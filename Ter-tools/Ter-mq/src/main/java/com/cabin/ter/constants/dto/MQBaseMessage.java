package com.cabin.ter.constants.dto;

import com.cabin.ter.constants.enums.SourceEnum;
import com.cabin.ter.constants.participant.msg.MessageParticipant;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <p>
 *     MQ 消息的基类，MQ发送的所有消息，必须继承该类
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-05 15:22
 */
@Data
public abstract class MQBaseMessage{
    /**
     * 发送消息来源，用于排查问题
     */
    protected SourceEnum source;
    /**
     * 消息跟踪id
     */
    protected String traceId = String.valueOf(UUID.randomUUID());
    /**
     * 发送时间
     */
    protected LocalDateTime sendTime = LocalDateTime.now();
    /**
     * 重试次数，用于判断重试次数，超过重试次数发送异常警告
     */
    protected Integer retryTimes = 0;
}
