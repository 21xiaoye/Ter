package com.cabin.ter.common.listener.consumer;

import com.cabin.ter.common.constants.dto.MQBaseMessage;
import com.cabin.ter.common.constants.participant.constant.TopicConstant;
import com.cabin.ter.common.constants.participant.msg.MessageParticipant;
import com.cabin.ter.common.constants.participant.msg.WebSocketWideParticipant;
import com.cabin.ter.common.listener.BaseMqMessageListener;
import lombok.extern.slf4j.Slf4j;
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
        topic = TopicConstant.SOURCE_BROADCASTING_GROUP,
        consumerGroup = TopicConstant.SOURCE_BROADCASTING_GROUP,
        consumeThreadNumber = 5
)
public class WideMessageListener <T extends MessageParticipant> extends BaseMqMessageListener implements RocketMQListener<WebSocketWideParticipant> {

    @Override
    protected void handleMessage(MQBaseMessage message) throws Exception {

    }

    @Override
    protected void handleMaxRetriesExceeded(MQBaseMessage message) {

    }

    @Override
    protected String ConsumerName() {
        return null;
    }

    @Override
    protected boolean isRetry() {
        return false;
    }

    @Override
    protected boolean throwException() {
        return false;
    }

    @Override
    public void onMessage(WebSocketWideParticipant webSocketWideParticipant) {

    }
}
