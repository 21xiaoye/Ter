package com.cabin.ter.constants.dto;

import com.cabin.ter.constants.enums.MessagePushMethodEnum;
import com.cabin.ter.constants.participant.msg.MessageParticipant;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
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
public abstract class MQBaseMessage implements MessageParticipant, Serializable {
    /**
     * 业务键，用于RocketMQ控制台查看消费情况
     */
    protected String key;
    /**
     * 发送消息来源，用于排查问题
     */
    protected String source;
    /**
     * 消息跟踪id
     */
    protected String traceId = UUID.randomUUID().toString();
    /**
     * 发送时间
     */
    protected LocalDateTime sendTime;
    /**
     * 重试次数，用于判断重试次数，超过重试次数发送异常警告
     */
    protected Integer retryTimes = 0;
    /**
     * 推送方式 (邮箱广播推送，短信推送，微信公众号推送......)
     * @see MessagePushMethodEnum
     */
    protected MessagePushMethodEnum pushMethod;
}
