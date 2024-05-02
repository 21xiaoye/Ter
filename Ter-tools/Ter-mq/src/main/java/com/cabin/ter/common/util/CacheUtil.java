package com.cabin.ter.common.util;

import com.cabin.ter.common.constants.entity.ws.ServerInfo;
import com.cabin.ter.common.websocket.WebsocketServer;
import io.netty.channel.Channel;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     缓存服务关键信息工具类
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-02 22:30
 */
public class CacheUtil {
    // 缓存channel
    public static Map<String, Channel> cacheChannel = Collections.synchronizedMap(new HashMap<>());

    // 缓存服务信息
    public static Map<Integer, ServerInfo> serverInfoMap = Collections.synchronizedMap(new HashMap<>());

    // 缓存 websocket 服务端
    public static Map<Integer, WebsocketServer> serverMap = Collections.synchronizedMap(new HashMap<>());
}
