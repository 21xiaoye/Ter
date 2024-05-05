package com.cabin.ter.common.config;


import com.alibaba.fastjson.JSON;
import com.cabin.ter.common.constants.participant.msg.WebSocketSingleParticipant;
import com.cabin.ter.common.util.CacheUtil;
import com.cabin.ter.common.util.MsgUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;


/**
 * <p>
 *     redis 订阅者
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-03 23:05
 */
@Slf4j
public class RedisChannelListener implements MessageListener{
    @Override
    public void onMessage(Message message, @Nullable byte[] pattern) {
        log.info("sub message :) channel[cleanNoStockCache] !");

        log.info("接收到PUSH消息：{}", message);
        WebSocketSingleParticipant msgAgreement = JSON.parseObject(message.getBody(), WebSocketSingleParticipant.class);
        String toChannelId = msgAgreement.getChannelId();
        Channel channel = CacheUtil.cacheChannel.get(toChannelId);
        if (null == channel) {
            return;
        }
        // 发送消息
        channel.writeAndFlush(new TextWebSocketFrame(MsgUtil.obj2Json(msgAgreement) + "  redis listener lalalalala "));
    }

}
