package com.cabin.ter.common.listener.consumer;

import com.cabin.ter.common.constants.dto.MQBaseMessage;
import com.cabin.ter.common.constants.enums.MessagePushMethodEnum;
import com.cabin.ter.common.constants.participant.constant.ConsumerNameConstant;
import com.cabin.ter.common.constants.participant.constant.TopicConstant;
import com.cabin.ter.common.constants.participant.msg.EmailParticipant;
import com.cabin.ter.common.constants.participant.msg.MessageParticipant;
import com.cabin.ter.common.constants.participant.msg.WebSocketSingleParticipant;
import com.cabin.ter.common.constants.participant.msg.WebSocketWideParticipant;
import com.cabin.ter.common.listener.BaseMqMessageListener;
import com.cabin.ter.common.service.BaseMessageStrategyService;
import com.cabin.ter.common.service.MessageStrategyServiceFactory;
import com.cabin.ter.common.util.CacheUtil;
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
            case USER_WEB_MESSAGE -> {
                for (Channel channel : CacheUtil.cacheChannel.values()) {
                    channel.writeAndFlush(new TextWebSocketFrame(webSocketWideMessage.getContent()+"Hello !"));
                }
            }
            default -> {
                log.error("推送方式不支持");
                throwException();
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
