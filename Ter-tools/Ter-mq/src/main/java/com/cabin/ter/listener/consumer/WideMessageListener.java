package com.cabin.ter.listener.consumer;

import com.cabin.ter.constants.dto.MQBaseMessage;
import com.cabin.ter.constants.enums.MessagePushMethodEnum;
import com.cabin.ter.constants.participant.constant.ConsumerNameConstant;
import com.cabin.ter.constants.participant.constant.TopicConstant;
import com.cabin.ter.constants.participant.msg.WebSocketWideParticipant;
import com.cabin.ter.listener.BaseMqMessageListener;
import com.cabin.ter.util.CacheUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * <p>
 *    广播消息 消费者
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-06 21:35
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = TopicConstant.ROCKETMQ_BROADCASTING_PUSH_MESSAGE_TOPIC,
        consumerGroup = TopicConstant.SOURCE_BROADCASTING_GROUP,
        selectorExpression = TopicConstant.SOURCE_BROADCASTING_WIND_TAG,
        messageModel = MessageModel.BROADCASTING,
        consumeMode = ConsumeMode.CONCURRENTLY,
        consumeThreadNumber = 5
)
public class WideMessageListener extends BaseMqMessageListener implements RocketMQListener<WebSocketWideParticipant>  {
    @Override
    protected void handleMessage(MQBaseMessage message) throws Exception {
        WebSocketWideParticipant webSocketWideMessage = (WebSocketWideParticipant)message;
        MessagePushMethodEnum pushMethod = webSocketWideMessage.getPushMethod();
        switch (pushMethod){
            /**
             * 这里暂且只给在线用户发送消息
             */
            case USER_WEB_MESSAGE -> {
                for (Channel channel : CacheUtil.cacheChannel.values()) {
                    channel.writeAndFlush(new TextWebSocketFrame(webSocketWideMessage.getContent()));
                }
            }
            default -> {
                log.error("推送失败,推送方式不支持");
            }
        }
    }

    @Override
    protected void handleMaxRetriesExceeded(MQBaseMessage message) {

    }

    @Override
    protected String ConsumerName() {
        return ConsumerNameConstant.WIDE_MESSAGE_CONSUMER;
    }

    @Override
    protected boolean isRetry() {
        return true;
    }

    @Override
    protected boolean throwException() {
        return false;
    }

    @Override
    public void onMessage(WebSocketWideParticipant webSocketWideParticipant) {
        super.dispatchMessage(webSocketWideParticipant);
    }
}
