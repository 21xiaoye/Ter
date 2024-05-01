package com.cabin.ter.common.service;

import com.cabin.ter.common.constants.entity.MessageParticipant;

/**
 * <p>
 *     消息发送基类
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-01 12:55
 */
public interface BaseMessageStrategyService {
    <T extends MessageParticipant> void awardStrategy(MessageParticipant message);
}
