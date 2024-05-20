package com.cabin.ter.util;

import com.cabin.ter.constants.dto.WSChannelExtraDTO;
import com.cabin.ter.constants.participant.ws.ServerInfo;
import com.cabin.ter.websocket.WebsocketServer;
import io.netty.channel.Channel;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 *     缓存服务关键信息工具类
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-02 22:30
 */
public class CacheUtil {
    /**
     * 用来存储用户未登录时的websocket连接
     *
     * 处理登录验证码发送、扫码成功消息推送等情况
     */
    public static Map<String, Channel> cacheChannel = Collections.synchronizedMap(new HashMap<>());
    /**
     * 缓存用户登录成功之后的信息
     */
    private static final ConcurrentHashMap<Channel, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();
    /**
     * 所有在线用户及其在线好友
     */
    private static final ConcurrentHashMap<Long, CopyOnWriteArrayList<Channel>> ONLINE_UID_MAP = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Channel, WSChannelExtraDTO> getOnlineMap(){return ONLINE_WS_MAP;}
    /**
     * 缓存服务信息
     */
    public static Map<Integer, ServerInfo> serverInfoMap = Collections.synchronizedMap(new HashMap<>());

    /**
     * 缓存 websocket 服务端信息
     */
    public static Map<Integer, WebsocketServer> serverMap = Collections.synchronizedMap(new HashMap<>());
}
