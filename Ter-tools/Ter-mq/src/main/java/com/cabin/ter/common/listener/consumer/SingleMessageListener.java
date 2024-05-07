package com.cabin.ter.common.listener.consumer;

import com.cabin.ter.common.constants.dto.MQBaseMessage;
import com.cabin.ter.common.constants.enums.MessagePushMethodEnum;
import com.cabin.ter.common.constants.participant.constant.ConsumerNameConstant;
import com.cabin.ter.common.constants.participant.constant.TopicConstant;
import com.cabin.ter.common.constants.participant.msg.EmailParticipant;
import com.cabin.ter.common.constants.participant.msg.MessageParticipant;
import com.cabin.ter.common.constants.participant.msg.WebSocketSingleParticipant;
import com.cabin.ter.common.listener.BaseMqMessageListener;
import com.cabin.ter.common.service.BaseMessageStrategyService;
import com.cabin.ter.common.service.MessageStrategyServiceFactory;
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
        selectorExpression = TopicConstant.SOURCE_SINGLE_PUSH_TAG,
        messageModel = MessageModel.BROADCASTING,
        consumeMode = ConsumeMode.CONCURRENTLY,
        consumeThreadNumber = 5
)
public class SingleMessageListener extends BaseMqMessageListener implements RocketMQListener<WebSocketSingleParticipant> {
    @Override
    protected void handleMessage(MQBaseMessage message) throws Exception {
        log.info("单点发送消费者开始进行消费了");
        WebSocketSingleParticipant singleMessage  = (WebSocketSingleParticipant) message;
        MessagePushMethodEnum pushMethod = singleMessage.getPushMethod();
        switch (pushMethod){
            case EMAIL_MESSAGE -> {
                log.info("开始进行消息消费了哦");
                BaseMessageStrategyService emailStrategy = MessageStrategyServiceFactory.getStrategy(MessagePushMethodEnum.EMAIL_MESSAGE);
                EmailParticipant emailMessageBuild = EmailParticipant.builder()
                        .subject(singleMessage.getSource())
                        .content(singleMessage.getContent())
                        .to(singleMessage.getToAddress())
                        .build();
                emailStrategy.messageStrategy(emailMessageBuild);
            }
            default -> {
                log.info("推送失败,推送方式不支持");
            }
        }
    }

    @Override
    protected void handleMaxRetriesExceeded(MQBaseMessage message) {
        log.info("进行事务处理");
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


    @Override
    public void onMessage(WebSocketSingleParticipant webSocketSingleParticipant) {
        super.dispatchMessage(webSocketSingleParticipant);
    }
}
