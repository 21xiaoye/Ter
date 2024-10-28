package com.cabin.ter.service.impl;

import com.cabin.ter.adapter.TextBuilder;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.admin.mapper.UserDomainMapper;
import com.cabin.ter.cache.RedisCache;
import com.cabin.ter.constants.RedisKey;
import com.cabin.ter.constants.enums.SourceEnum;
import com.cabin.ter.constants.TopicConstant;
import com.cabin.ter.service.WxMsgService;
import com.cabin.ter.template.RocketMQEnhanceTemplate;
import com.cabin.ter.adapter.MQMessageBuilderAdapter;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.net.URLEncoder;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaoye
 * @date Created in 2024-05-11 11:29
 */
@Slf4j
@Service
public class WxMsgServiceImpl implements WxMsgService {
    private static final String SCAN_QR_PREFIX = "qrscene_";
    private static final String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
    @Value("${wx.mp.callback}")
    private String callback;
    @Autowired
    private UserDomainMapper userDomainMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private RocketMQEnhanceTemplate rocketMQTemplate;


    /**
     * 用户扫码
     * @param wxMpService
     * @param wxMpXmlMessage
     * @return
     */
    @Override
    public WxMpXmlOutMessage scan(WxMpService wxMpService, WxMpXmlMessage wxMpXmlMessage)  {
        String openId = wxMpXmlMessage.getFromUser();
        Integer loginCode = Integer.parseInt(getEventKey(wxMpXmlMessage));

        UserDomain userDomain = userDomainMapper.findByUserOpenId(openId);

        /**
         * 数据库有用户 openId 记录，直接通知登录成功，无需授权
         */
        if (Objects.nonNull(userDomain)) {
            rocketMQTemplate.send(TopicConstant.LOGIN_MSG_TOPIC, MQMessageBuilderAdapter.buildLoginMessage(openId, userDomain.getUserEmail(), loginCode, SourceEnum.WX_SCAN_CODE_SOURCE));
            log.info("用户扫码登录成功");
            return null;
        }

        /**
         * 通知用户进行授权
         */
        redisCache.set(RedisKey.getKey(RedisKey.OPEN_ID_STRING, openId), loginCode, 60, TimeUnit.MINUTES);

        rocketMQTemplate.send(TopicConstant.SCAN_MSG_TOPIC, MQMessageBuilderAdapter.buildScanSuccessMessage(openId, loginCode));

        String skipUrl = String.format(URL,  wxMpService.getWxMpConfigStorage().getAppId(), URLEncoder.encode( callback+"/wx/portal/public/callBack"));
        WxMpXmlOutMessage.TEXT().build();

        log.info("授权链接{}",skipUrl);
        return new TextBuilder().build("请点击链接授权完成登录：<a href=\"" + skipUrl + "\">授权</a>", wxMpXmlMessage, wxMpService);

    }

    private String getEventKey(WxMpXmlMessage wxMpXmlMessage){
        return wxMpXmlMessage.getEventKey().replace(SCAN_QR_PREFIX,"");
    }

    /**
     * 用户授权成功
     * */
    @Override
    public void authorize(WxOAuth2UserInfo userInfo) {

//        Integer loginCode = redisCache.get(RedisKey.getKey(RedisKey.OPEN_ID_STRING, userInfo.getOpenid()), Integer.class);
        /**
         * 用户授权成功，保存用户信息
         */
        log.info("微信用户的信息{}",userInfo);
        redisCache.set(RedisKey.getKey(RedisKey.AUTHORIZE_WX,userInfo.getOpenid()),userInfo,60,TimeUnit.MINUTES);
        /**
         * 通知用户进行邮箱绑定
         */
//        rocketMQTemplate.send(TopicConstant.EMAIL_BINDING_TOPIC,new EmailBindingDTO(loginCode, userInfo.getOpenid()));
    }
}
