package com.cabin.ter.listener.consumer;

import com.cabin.ter.constants.dto.ChatMessageDTO;
import com.cabin.ter.constants.participant.constant.ConsumerNameConstant;
import com.cabin.ter.constants.TopicConstant;
import com.cabin.ter.listener.BaseMqMessageListener;
import com.cabin.ter.service.WebSocketPublicService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@RocketMQMessageListener(
        topic = TopicConstant.CHAT_MESSAGE_SEND_TOPIC,
        consumerGroup = TopicConstant.CHAT_MESSAGE_SEND_GROUP,
        messageModel = MessageModel.BROADCASTING
)
@Component
@Slf4j
public class ChatMessageConsumer extends BaseMqMessageListener<ChatMessageDTO> implements RocketMQListener<ChatMessageDTO> {
    @Autowired
    private WebSocketPublicService webSocketService;

    @Override
    public void onMessage(ChatMessageDTO message) {
        super.dispatchMessage(message);
    }

    @Override
    protected void handleMessage(ChatMessageDTO message) {
        message.getUserIdList().forEach(userId -> {
            webSocketService.sendToUid(message.getWsBaseResp(), userId);
        });
    }

    @Override
    protected void handleMaxRetriesExceeded(ChatMessageDTO message) {
        log.info("{}消息发送失败",message);
    }

    @Override
    protected String ConsumerName() {
        return ConsumerNameConstant.CHAT_MESSAGE_SEND_CONSUMER;
    }

    @Override
    protected boolean isRetry() {
        return false;
    }

    @Override
    protected boolean throwException() {
        return false;
    }
}