package com.cabin.ter.counsumer;

import com.cabin.ter.constants.dto.PushMessageDTO;
import com.cabin.ter.constants.enums.WSPushTypeEnum;
import com.cabin.ter.constants.participant.constant.ConsumerNameConstant;
import com.cabin.ter.constants.participant.constant.TopicConstant;
import com.cabin.ter.listener.BaseMqMessageListener;
import com.cabin.ter.service.WebSocketPublicService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RocketMQMessageListener(topic = TopicConstant.PUSH_TOPIC, consumerGroup = TopicConstant.PUSH_GROUP, messageModel = MessageModel.BROADCASTING)
@Component
@Slf4j
public class PushUserConsumer extends BaseMqMessageListener<PushMessageDTO> implements RocketMQListener<PushMessageDTO> {
    @Autowired
    private WebSocketPublicService webSocketService;

    @Override
    public void onMessage(PushMessageDTO message) {
        super.dispatchMessage(message);
    }

    @Override
    protected void handleMessage(PushMessageDTO message) throws Exception {
        WSPushTypeEnum wsPushTypeEnum = WSPushTypeEnum.of(message.getPushType());
        switch (wsPushTypeEnum) {
            case USER:
                message.getUidList().forEach(uid -> {
                    webSocketService.sendToUid(message.getWsBaseMsg(), uid);
                });
                break;
            case ALL:
                webSocketService.sendToAllOnline(message.getWsBaseMsg());
                break;
        }
    }

    @Override
    protected void handleMaxRetriesExceeded(PushMessageDTO message) {
        log.info("{}消息发送失败",message);
    }

    @Override
    protected String ConsumerName() {
        return ConsumerNameConstant.PUSH_USER_CONSUMER;
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