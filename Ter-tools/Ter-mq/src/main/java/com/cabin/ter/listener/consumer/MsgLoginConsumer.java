package com.cabin.ter.listener.consumer;

import com.cabin.ter.constants.dto.LoginMessageDTO;
import com.cabin.ter.constants.dto.MQBaseMessage;
import com.cabin.ter.constants.participant.constant.ConsumerNameConstant;
import com.cabin.ter.constants.participant.constant.TopicConstant;
import com.cabin.ter.listener.BaseMqMessageListener;
import com.cabin.ter.service.WebSocketPublicService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 *     在本地服务上找寻对应channel，将对应用户登陆，并触发所有用户收到上线事件
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-19 19:10
 */
@RocketMQMessageListener(
        consumerGroup = TopicConstant.LOGIN_MSG_GROUP,
        topic = TopicConstant.LOGIN_MSG_TOPIC,
        messageModel = MessageModel.BROADCASTING,
        consumeMode = ConsumeMode.CONCURRENTLY,
        consumeThreadNumber = 5
)
@Component
@Slf4j
public class MsgLoginConsumer extends BaseMqMessageListener<LoginMessageDTO> implements RocketMQListener<LoginMessageDTO> {
    @Autowired
    private WebSocketPublicService webSocketService;

    @Override
    public void onMessage(LoginMessageDTO loginMessageDTO) {
        super.dispatchMessage(loginMessageDTO);
    }

    @Override
    protected void handleMessage(LoginMessageDTO message) {
        webSocketService.scanLoginSuccess(message.getOpenId(),message.getLogonCode(),message.getLoginEmail());
    }

    @Override
    protected void handleMaxRetriesExceeded(LoginMessageDTO message) {
        log.info("进行事务处理......");
    }

    @Override
    protected String ConsumerName() {
        return ConsumerNameConstant.WX_SCAN_CODE_MESSAGE_CONSUMER;
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
