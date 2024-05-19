package com.cabin.ter.service.impl;

import cn.hutool.json.JSONUtil;
import com.cabin.ter.adapter.WSAdapter;
import com.cabin.ter.cache.RedisCache;
import com.cabin.ter.constants.RedisKey;
import com.cabin.ter.constants.participant.msg.WebSocketSingleParticipant;
import com.cabin.ter.constants.vo.response.WSBaseResp;
import com.cabin.ter.service.WebSocketPublicService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
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
    public Boolean scanLoginSuccess(Integer loginCode, Long uid) {
        return null;
    }

    @Override
    public Boolean scanSuccess(Integer loginCode) {
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(loginCode);
        if(Objects.nonNull(channel)){
            sendMsg(channel, WSAdapter.buildScanSuccessResp());
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    private void sendMsg(Channel channel, WSBaseResp<?> wsBaseResp){
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(wsBaseResp)));
    }
}




















