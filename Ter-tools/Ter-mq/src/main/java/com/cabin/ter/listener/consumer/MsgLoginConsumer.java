package com.cabin.ter.listener.consumer;

import com.cabin.ter.constants.dto.LoginMessageDTO;
import com.cabin.ter.constants.participant.constant.TopicConstant;
import com.cabin.ter.service.WebSocketPublicService;
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
        messageModel = MessageModel.BROADCASTING
)
@Component
public class MsgLoginConsumer implements RocketMQListener<LoginMessageDTO> {
    @Autowired
    private WebSocketPublicService webSocketService;

    @Override
    public void onMessage(LoginMessageDTO loginMessageDTO) {
        //尝试登录登录
        webSocketService.scanLoginSuccess(loginMessageDTO.getOpenId(),loginMessageDTO.getLogonCode(),loginMessageDTO.getLoginEmail());
    }

}
