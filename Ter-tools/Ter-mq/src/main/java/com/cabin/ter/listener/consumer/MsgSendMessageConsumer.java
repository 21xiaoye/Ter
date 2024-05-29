package com.cabin.ter.listener.consumer;

import com.cabin.ter.constants.dto.MsgSendMessageDTO;
import com.cabin.ter.constants.participant.constant.TopicConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author xiaouye
 * @date Created in 2024-05-29 18:31
 */
@Slf4j
@RocketMQMessageListener(consumerGroup = TopicConstant.SEND_MSG_GROUP, topic = TopicConstant.SEND_MSG_TOPIC)
@Component
public class MsgSendMessageConsumer implements RocketMQListener<MsgSendMessageDTO> {

    @Override
    public void onMessage(MsgSendMessageDTO msgSendMessageDTO) {
        log.info("收到消息{}",msgSendMessageDTO);
    }
}
