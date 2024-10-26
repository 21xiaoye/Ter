package com.cabin.ter.listener.listener;

import com.cabin.ter.constants.participant.constant.TopicConstant;
import com.cabin.ter.listener.event.MessageSendEvent;
import com.cabin.ter.template.RocketMQEnhanceTemplate;
import com.cabin.ter.adapter.MQMessageBuilderAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * <p>
 *     发送消息监听器
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-29 15:46
 */
@Component
@Slf4j
public class MessageSendListener {
    @Autowired
    private RocketMQEnhanceTemplate rocketMQEnhanceTemplate;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, classes = MessageSendEvent.class, fallbackExecution = true)
    public void messageRoute(MessageSendEvent event) {
        Long msgId = event.getMsgId();

        rocketMQEnhanceTemplate.sendSecureMsg(TopicConstant.SEND_MSG_TOPIC, MQMessageBuilderAdapter.buildMsgSendMessage(msgId));
    }

}
