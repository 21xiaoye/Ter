package com.cabin.ter.template;

import com.cabin.ter.constants.dto.MQBaseMessage;
import com.cabin.ter.constants.participant.msg.MessageParticipant;
import com.cabin.ter.service.MessageProcessing;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *     消息发送接口
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-01 12:55
 */
@Slf4j
public abstract class MessageTemplate<T extends MQBaseMessage>{
    /**
     * 执行消息推送
     *
     * @return
     */
    protected abstract Boolean messageSend(T message);

}
