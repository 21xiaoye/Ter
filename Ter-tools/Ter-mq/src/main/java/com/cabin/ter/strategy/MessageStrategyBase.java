package com.cabin.ter.strategy;

import com.cabin.ter.constants.dto.MQBaseMessage;
import com.cabin.ter.constants.enums.MessagePushMethodEnum;

/**
 * <p>
 *     消息发送基类
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-01 12:55
 */
public interface MessageStrategyBase {
    <T extends MQBaseMessage> Boolean messageStrategy(T message);
    MessagePushMethodEnum getSource();
}
