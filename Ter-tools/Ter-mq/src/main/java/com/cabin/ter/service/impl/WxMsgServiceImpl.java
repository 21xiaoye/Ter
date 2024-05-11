package com.cabin.ter.service.impl;

import com.cabin.ter.adapter.TextBuilder;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.admin.mapper.UserDomainMapper;
import com.cabin.ter.cache.RedisCache;
import com.cabin.ter.constants.RedisKey;
import com.cabin.ter.constants.dto.LoginMessageDTO;
import com.cabin.ter.constants.dto.ScanSuccessMessageDTO;
import com.cabin.ter.constants.participant.constant.TopicConstant;
import com.cabin.ter.service.UserService;
import com.cabin.ter.service.WxMsgService;
import com.cabin.ter.template.RocketMQEnhanceTemplate;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
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
@Service
public class WxMsgServiceImpl implements WxMsgService {
    private static final String SCAN_QR_PREFIX = "qrscene_";
    private static final String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
    @Value("${wx.mp.callback}")
    private String callback;
    @Autowired
    private UserDomainMapper userDomainMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisCache redisCachel;
    @Autowired
    private RocketMQEnhanceTemplate rocketMQTemplate;


    /**
     * 用户扫码
     * @param wxMpService
     * @param wxMpXmlMessage
     * @return
     */
    @Override
    public WxMpXmlOutMessage scan(WxMpService wxMpService, WxMpXmlMessage wxMpXmlMessage) {
        String openid = wxMpXmlMessage.getFromUser();
        Integer loginCode = Integer.parseInt(getEventKey(wxMpXmlMessage));

        UserDomain userDomain = userDomainMapper.findByUserOpenId(openid);

        if (Objects.nonNull(userDomain) && StringUtils.isNotEmpty(userDomain.getUserAvatar())) {
            rocketMQTemplate.send(TopicConstant.LOGIN_MSG_TOPIC, new LoginMessageDTO(userDomain.getUserId(), loginCode));
            return null;
        }

        //user为空先注册,手动生成,以保存uid
        if (Objects.isNull(userDomain)) {
            userDomain = UserDomain.builder().openId(openid).build();
            userService.register(userDomain);
        }
        //在redis中保存openid和场景code的关系，后续才能通知到前端,旧版数据没有清除,这里设置了过期时间
        redisCachel.set(RedisKey.getKey(RedisKey.OPEN_ID_STRING, openid), loginCode, 60, TimeUnit.MINUTES);
        //授权流程,给用户发送授权消息，并且异步通知前端扫码成功,等待授权
        rocketMQTemplate.send(TopicConstant.SCAN_MSG_TOPIC, new ScanSuccessMessageDTO(loginCode));
        String skipUrl = String.format(URL, wxMpService.getWxMpConfigStorage().getAppId(), URLEncoder.encode( callback+"/wx/portal/public/callBack"));
        WxMpXmlOutMessage.TEXT().build();


        return new TextBuilder().build("请点击链接授权：<a href=\"" + skipUrl + "\">登录</a>", wxMpXmlMessage, wxMpService);
    }

    private String getEventKey(WxMpXmlMessage wxMpXmlMessage){
        return wxMpXmlMessage.getEventKey().replace(SCAN_QR_PREFIX,"");
    }

    /**
     * 用户授权
     * */
    @Override
    public void authorize(WxOAuth2UserInfo userInfo) {
    }
}
