package com.cabin.ter.service;

import com.cabin.ter.constants.participant.msg.MessageParticipant;
import com.cabin.ter.constants.enums.MessagePushMethodEnum;

/**
 * <p>
 *     消息发送基类
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-01 12:55
 */
public interface BaseMessageStrategyService {
    <T extends MessageParticipant> Boolean messageStrategy(MessageParticipant message);
    MessagePushMethodEnum getSource();
}
