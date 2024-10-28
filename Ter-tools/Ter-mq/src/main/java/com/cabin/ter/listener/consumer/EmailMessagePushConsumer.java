package com.cabin.ter.listener.consumer;

import com.cabin.ter.constants.enums.MessagePushMethodEnum;
import com.cabin.ter.constants.participant.constant.ConsumerNameConstant;
import com.cabin.ter.constants.TopicConstant;
import com.cabin.ter.constants.dto.EmailMessageDTO;
import com.cabin.ter.listener.BaseMqMessageListener;
import com.cabin.ter.strategy.MessageStrategyBase;
import com.cabin.ter.strategy.MessageStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
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
        topic = TopicConstant.ROCKET_SINGLE_PUSH_MESSAGE_TOPIC,
        consumerGroup = TopicConstant.SINGLE_SINGLE_PUSH_MESSAGE_GROUP,
        messageModel = MessageModel.BROADCASTING,
        consumeMode = ConsumeMode.CONCURRENTLY,
        consumeThreadNumber = 5
)
public class EmailMessagePushConsumer extends BaseMqMessageListener<EmailMessageDTO> implements RocketMQListener<EmailMessageDTO> {
    @Override
    public void onMessage(EmailMessageDTO emailParticipant) {
        super.dispatchMessage(emailParticipant);
    }

    @Override
    protected void handleMessage(EmailMessageDTO message) {
        log.info("收到消息[{]]",message);
        MessageStrategyBase emailStrategy = MessageStrategyFactory.getStrategy(MessagePushMethodEnum.EMAIL_MESSAGE);
        emailStrategy.messageStrategy(message);
    }

    @Override
    protected void handleMaxRetriesExceeded(EmailMessageDTO message) {
        log.info("进行事务补偿处理......");
    }

    @Override
    protected String ConsumerName() {
        return ConsumerNameConstant.SINGLE_MESSAGE_CONSUMER;
    }

    @Override
    protected boolean isRetry() {
        return true;
    }

    @Override
    protected boolean throwException() {
        return false;
    }
}


