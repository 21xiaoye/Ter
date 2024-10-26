package com.cabin.ter.listener.consumer;

import com.cabin.ter.constants.dto.ScanSuccessMessageDTO;
import com.cabin.ter.constants.participant.constant.TopicConstant;
import com.cabin.ter.service.WebSocketPublicService;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 *     将扫码成功的信息发送给对应的用户,等待授权
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-19 19:06
 */
@RocketMQMessageListener(
        consumerGroup = TopicConstant.SCAN_MSG_GROUP,
        topic = TopicConstant.SCAN_MSG_TOPIC,
        messageModel = MessageModel.BROADCASTING
)
@Component
public class ScanSuccessConsumer implements RocketMQListener<ScanSuccessMessageDTO> {
    @Autowired
    private WebSocketPublicService webSocketService;

    @Override
    public void onMessage(ScanSuccessMessageDTO scanSuccessMessageDTO) {
        webSocketService.scanSuccess(scanSuccessMessageDTO.getLoginCode());
    }
}
