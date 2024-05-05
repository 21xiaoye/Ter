package com.cabin.ter.common.listener.consumer;

import com.cabin.ter.common.constants.dto.MQBaseMessage;
import com.cabin.ter.common.constants.participant.ConsumerName;
import com.cabin.ter.common.constants.participant.TopicConstant;
import com.cabin.ter.common.constants.participant.msg.MessageParticipant;
import com.cabin.ter.common.constants.participant.msg.WebSocketSingleParticipant;
import com.cabin.ter.common.listener.BaseMqMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author xiaoye
 * @date Created in 2024-05-05 21:09
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = TopicConstant.SOURCE_BROADCASTING_GROUP,
        consumerGroup = TopicConstant.SOURCE_BROADCASTING_GROUP,
        consumeThreadNumber = 5
)
public class RocketEntityMessageListener<T extends MessageParticipant> extends BaseMqMessageListener implements RocketMQListener<WebSocketSingleParticipant> {
    @Override
    protected void handleMessage(MQBaseMessage message) throws Exception {
        log.info("进行消息处理");
    }

    @Override
    protected void handleMaxRetriesExceeded(MQBaseMessage message) {
        log.info("进行事务处理");
    }

    @Override
    protected String ConsumerName() {
        return ConsumerName.SING_MESSAGE_CONSUMER;
    }

    @Override
    protected boolean isRetry() {
        return true;
    }

    @Override
    protected boolean filter(MQBaseMessage message) {
        return false;
    }

    @Override
    protected boolean throwException() {
        return false;
    }


    @Override
    public void onMessage(WebSocketSingleParticipant webSocketSingleParticipant) {
        super.dispatchMessage(webSocketSingleParticipant);
    }
}
