package com.cabin.ter.listener.consumer;

import com.cabin.ter.constants.dto.MQBaseMessage;
import com.cabin.ter.constants.enums.MessagePushMethodEnum;
import com.cabin.ter.constants.participant.constant.ConsumerNameConstant;
import com.cabin.ter.constants.participant.constant.TopicConstant;
import com.cabin.ter.constants.participant.msg.EmailParticipant;
import com.cabin.ter.constants.participant.msg.WebSocketSingleParticipant;
import com.cabin.ter.listener.BaseMqMessageListener;
import com.cabin.ter.service.BaseMessageStrategyService;
import com.cabin.ter.service.MessageStrategyServiceFactory;
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
    protected void handleMessage(MQBaseMessage message) {
        WebSocketSingleParticipant singleMessage  = (WebSocketSingleParticipant) message;
        MessagePushMethodEnum pushMethod = singleMessage.getPushMethod();
        switch (pushMethod){
            case EMAIL_MESSAGE -> {
                BaseMessageStrategyService emailStrategy = MessageStrategyServiceFactory.getStrategy(MessagePushMethodEnum.EMAIL_MESSAGE);
                EmailParticipant emailMessageBuild = EmailParticipant.builder()
                        .subject(singleMessage.getSource())
                        .content(singleMessage.getContent())
                        .to(singleMessage.getToAddress())
                        .build();
                emailStrategy.messageStrategy(emailMessageBuild);
            }
            /**
             * 这里暂且只给在线用户推送消息
             */
            case USER_WEB_MESSAGE -> {
                Channel channel = CacheUtil.cacheChannel.get(singleMessage.getToAddress());
                channel.writeAndFlush(new TextWebSocketFrame(singleMessage.getContent()));
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
