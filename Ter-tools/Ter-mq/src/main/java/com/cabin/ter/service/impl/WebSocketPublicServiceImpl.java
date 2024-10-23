package com.cabin.ter.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.cabin.ter.adapter.WSAdapter;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.cache.RedisCache;
import com.cabin.ter.config.ThreadPoolConfig;
import com.cabin.ter.constants.RedisKey;
import com.cabin.ter.constants.dto.EmailBindingDTO;
import com.cabin.ter.constants.dto.WSChannelExtraDTO;
import com.cabin.ter.constants.vo.request.WSAuthorize;
import com.cabin.ter.constants.vo.response.WSBaseResp;
import com.cabin.ter.listener.event.UserOfflineEvent;
import com.cabin.ter.listener.event.UserOnlineEvent;
import com.cabin.ter.service.CustomUserDetailService;
import com.cabin.ter.service.WebSocketPublicService;
import com.cabin.ter.cache.UserCache;
import com.cabin.ter.util.JwtUtil;
import com.cabin.ter.constants.vo.response.JwtResponse;
import com.cabin.ter.util.NettyUtil;
import com.cabin.ter.vo.UserPrincipal;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
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
    /**
     * 所有已连接的websocket连接列表和一些额外参数
     */
    private static final ConcurrentHashMap<Channel, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();
    /**
     * 所有在线的用户和对应的socket
     */
    private static final ConcurrentHashMap<Long, CopyOnWriteArrayList<Channel>> ONLINE_UID_MAP = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Channel, WSChannelExtraDTO> getOnlineMap() {
        return ONLINE_WS_MAP;
    }
    private static final String LOGIN_CODE = "login code";

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private CustomUserDetailService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    @Qualifier(ThreadPoolConfig.WS_EXECUTOR)
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private UserCache userCache;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

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
        ONLINE_WS_MAP.put(channel, new WSChannelExtraDTO());
    }
    @Override
    public void removed(Channel channel) {
        WSChannelExtraDTO wsChannelExtraDTO = ONLINE_WS_MAP.get(channel);
        Optional<Long> uidOptional = Optional.ofNullable(wsChannelExtraDTO)
                .map(WSChannelExtraDTO::getUid);
        boolean offline = offline(channel, uidOptional);
        // 登录用户下线
        if(uidOptional.isPresent() && offline){
            UserDomain userDomain = new UserDomain();
            userDomain.setUserId(uidOptional.get());
            userDomain.setLastOptTime(System.currentTimeMillis());

            applicationEventPublisher.publishEvent(new UserOfflineEvent(this,userDomain));
        }
    }

    /**
     * 用户是否下线成功
     *
     * @param channel
     * @param uidOptional
     * @return
     */
    // TODO: 这里没有清空则自己手动清空，但是按理来说是自动清空的，因为离西安自动调用remote方法
    private boolean offline(Channel channel, Optional<Long> uidOptional){
        ONLINE_WS_MAP.remove(channel);
        if(uidOptional.isPresent()){
            CopyOnWriteArrayList<Channel> channels = ONLINE_UID_MAP.get(uidOptional.get());
            if(CollectionUtil.isNotEmpty(channels)){
                channels.removeIf(ch-> Objects.equals(ch,channels));
            }else {
                ONLINE_UID_MAP.remove(uidOptional);
            }
        }
        return true;
    }
    /**
     * 主动认证登录
     *
     * @param channel
     * @param wsAuthorize
     */
    public void authorize(Channel channel, WSAuthorize wsAuthorize){
        boolean b = jwtUtil.verityToken(wsAuthorize.getToken());
        // 通知前端 token 失效
        if(!b){
            sendMsg(channel, WSAdapter.buildInvalidateTokenResp());
        }else{
            this.online(channel,jwtUtil.getUIDFromJWT(wsAuthorize.getToken()));
        }
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

    @Override
    public void sendToUid(WSBaseResp<?> wsBaseResp, Long uid) {
        CopyOnWriteArrayList<Channel> channels = ONLINE_UID_MAP.get(uid);
        if(CollectionUtil.isEmpty(channels)){
            log.info("用户：{}不在线",uid);
            return;
        }
        // TODO:这里对一个用户所有连接推送，会导致一个用户被推送多次
        channels.forEach(channel -> {
            threadPoolTaskExecutor.execute(() -> sendMsg(channel, wsBaseResp));
        });
    }

    @Override
    public void sendToAllOnline(WSBaseResp<?> wsBaseResp, Long skipUid) {
        ONLINE_WS_MAP.forEach((channel, ext) -> {
            if (Objects.nonNull(skipUid) && Objects.equals(ext.getUid(), skipUid)) {
                return;
            }
            threadPoolTaskExecutor.execute(() -> sendMsg(channel, wsBaseResp));
        });
    }

    @Override
    public void sendToAllOnline(WSBaseResp<?> wsBaseResp) {
        this.sendToAllOnline(wsBaseResp, null);
    }

    /**
     * (channel必在本地)登录成功，并更新状态
     */
    private void loginSuccess(Channel channel, UserPrincipal user, JwtResponse token) {
        sendMsg(channel, WSAdapter.buildLoginSuccessResp(user, token.getToken()));
        this.online(channel, user.getUserId());
    }

    /**
     * 用户上线
     */
    private void online(Channel channel, Long uid) {
        getOrInitChannelExt(channel).setUid(uid);
        ONLINE_UID_MAP.putIfAbsent(uid, new CopyOnWriteArrayList<>());
        ONLINE_UID_MAP.get(uid).add(channel);
        log.info("用户再次上线{}通道为{}",uid,ONLINE_UID_MAP.get(uid));
        ONLINE_WS_MAP.put(channel,new WSChannelExtraDTO(uid));

        onlineNotification(uid);
    }
    public void onlineNotification(Long userId){
        boolean online = userCache.isOnline(userId);
        if(!online){
            UserPrincipal userPrincipal = new UserPrincipal();
            userPrincipal.setLastOptTime(new Date());
            userPrincipal.setUserId(userId);
            log.info("发送上线事件");
            applicationEventPublisher.publishEvent(new UserOnlineEvent(this, userPrincipal));
        }
    }
    /**
     * 如果在线列表不存在，就先把该channel放进在线列表
     *
     * @param channel
     * @return
     */
    private WSChannelExtraDTO getOrInitChannelExt(Channel channel) {
        WSChannelExtraDTO wsChannelExtraDTO =
                ONLINE_WS_MAP.getOrDefault(channel, new WSChannelExtraDTO());

        WSChannelExtraDTO old = ONLINE_WS_MAP.putIfAbsent(channel, wsChannelExtraDTO);
        return ObjectUtil.isNull(old) ? wsChannelExtraDTO : old;
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




















