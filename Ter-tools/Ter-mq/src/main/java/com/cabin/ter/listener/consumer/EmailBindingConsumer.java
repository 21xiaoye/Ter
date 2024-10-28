package com.cabin.ter.listener.consumer;

import com.cabin.ter.constants.dto.EmailBindingDTO;
import com.cabin.ter.constants.TopicConstant;
import com.cabin.ter.service.WebSocketPublicService;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 *     通知邮箱绑定mq
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-20 10:07
 */
@RocketMQMessageListener(
        consumerGroup = TopicConstant.EMAIL_BIDING_GROUP,
        topic = TopicConstant.EMAIL_BINDING_TOPIC,
        messageModel = MessageModel.BROADCASTING
)
@Component
public class EmailBindingConsumer implements RocketMQListener<EmailBindingDTO> {
    @Autowired
    private WebSocketPublicService webSocketPublicService;

    @Override
    public void onMessage(EmailBindingDTO emailBindingDTO) {
        webSocketPublicService.emailBinding(emailBindingDTO);
    }
}
