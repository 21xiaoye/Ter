package com.cabin.ter.common.service.impl;

import com.cabin.ter.common.constants.participant.msg.WebSocketSingleParticipant;
import com.cabin.ter.common.service.WebSocketPublicService;
import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author xiaoye
 * @date Created in 2024-05-03 21:11
 */
public class WebSocketServiceImpl implements WebSocketPublicService {
    /**
     * 所有已连接的websocket连接列表和一些额外参数
     */
    private static final ConcurrentHashMap<Channel, WebSocketSingleParticipant> ONLINE_WS_MAP = new ConcurrentHashMap<>();
    /**
     * 所有在线的用户和对应的socket
     */
    private static final ConcurrentHashMap<String, CopyOnWriteArrayList<Channel>> ONLINE_UID_MAP = new ConcurrentHashMap<>();

    /**
     * 返回用户登录二维码
     * @param channel
     */
    @Override
    public void handleLoginReq(Channel channel) {

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
        return null;
    }
}
