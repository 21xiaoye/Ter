package com.cabin.ter.service.impl;

import cn.hutool.json.JSONUtil;
import com.cabin.ter.adapter.WSAdapter;
import com.cabin.ter.cache.RedisCache;
import com.cabin.ter.constants.RedisKey;
import com.cabin.ter.constants.dto.EmailBindingDTO;
import com.cabin.ter.constants.vo.response.WSBaseResp;
import com.cabin.ter.service.CustomUserDetailService;
import com.cabin.ter.service.WebSocketPublicService;
import com.cabin.ter.util.JwtUtil;
import com.cabin.ter.constants.vo.response.JwtResponse;
import com.cabin.ter.vo.UserPrincipal;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaoye
 * @date Created in 2024-05-03 21:11
 */
@Slf4j
@Service
public class WebSocketPublicServiceImpl implements WebSocketPublicService {
    private static final Duration EXPIRE_TIME = Duration.ofHours(1);
    private static final Long MAX_MUM_SIZE = 10000L;
    public static final Cache<Integer, Channel> WAIT_LOGIN_MAP = Caffeine.newBuilder()
            .expireAfterWrite(EXPIRE_TIME)
            .maximumSize(MAX_MUM_SIZE)
            .build();

    private static final String LOGIN_CODE = "login code";

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private CustomUserDetailService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 返回用户登录二维码
     * @param channel
     */
    @Override
    public void handleLoginReq(Channel channel){
        try {
            int code = generateLoginCode(channel);
            WxMpQrCodeTicket wxMpQrCodeTicket =  wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code, (int) EXPIRE_TIME.getSeconds());
            log.info(channel.id()+"生成二维码"+wxMpQrCodeTicket);
            sendMsg(channel, WSAdapter.buildLoginResp(wxMpQrCodeTicket));
        }catch (WxErrorException e){
            throw new RuntimeException("二维码生成失败"+e);
        }
    }

    private Integer generateLoginCode(Channel channel){
        int code;
        do {
            code = redisCache.integerInc(RedisKey.getKey(LOGIN_CODE),(int)EXPIRE_TIME.toMillis(), TimeUnit.MINUTES);
        }while (WAIT_LOGIN_MAP.asMap().containsKey(code));
        WAIT_LOGIN_MAP.put(code, channel);
        return code;
    }

    @Override
    public void connect(Channel channel) {

    }

    @Override
    public void removed(Channel channel) {

    }

    @Override
    public Boolean scanLoginSuccess(String openId,Integer loginCode, String loginEmail) {
        //确认连接在该机器
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(loginCode);
        if (Objects.isNull(channel)) {
            return Boolean.FALSE;
        }
        WAIT_LOGIN_MAP.invalidate(loginCode);
        UserPrincipal userDetails = (UserPrincipal)userDetailsService.loadUserByUsername(loginEmail);
        String jwt = jwtUtil.createJWT(true, userDetails.getUserId(), userDetails.getUserEmail(), userDetails.getRoles(), userDetails.getAuthorities());
        JwtResponse jwtResponse = new JwtResponse(jwt);

        WxOAuth2UserInfo userInfo = redisCache.get(RedisKey.getKey(RedisKey.AUTHORIZE_WX, openId), WxOAuth2UserInfo.class);
        log.info("拿到微信信息"+userInfo);
        userDetails.setUserAvatar(userInfo.getHeadImgUrl());
        userDetails.setUserName(userInfo.getNickname());
        this.loginSuccess(channel,userDetails, jwtResponse);
        return Boolean.TRUE;
    }

    @Override
    public Boolean scanSuccess(Integer loginCode) {
        Channel channel = checkLoginCode(loginCode);
        if(Objects.nonNull(channel)){
            sendMsg(channel, WSAdapter.buildScanSuccessResp());
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean emailBinding(EmailBindingDTO emailBindingDTO) {
        Channel channel = checkLoginCode(emailBindingDTO.getCode());
        if(Objects.nonNull(channel)){
            sendMsg(channel,WSAdapter.buildEmailBindingResp(emailBindingDTO));
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    /**
     * (channel必在本地)登录成功，并更新状态
     */
    private void loginSuccess(Channel channel, UserPrincipal user, JwtResponse token) {
        sendMsg(channel, WSAdapter.buildLoginSuccessResp(user, token.getToken()));
    }

    private Channel checkLoginCode(Integer loginCode){
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(loginCode);
        if(Objects.nonNull(channel)){
            return channel;
        }
        return null;
    }
    private void sendMsg(Channel channel, WSBaseResp<?> wsBaseResp){
        log.info("推送用户消息{}",wsBaseResp);
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(wsBaseResp)));
    }
}




















