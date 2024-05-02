package com.cabin.ter.common.service;

import com.cabin.ter.common.constants.entity.msg.MessageParticipant;
import com.cabin.ter.common.constants.enums.MessageEnum;

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
    MessageEnum getSource();
}
