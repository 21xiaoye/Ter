package com.cabin.ter.template;

import com.alibaba.fastjson.JSONObject;
import com.cabin.ter.constants.dto.LoginMessageDTO;
import com.cabin.ter.constants.dto.MQBaseMessage;
import com.cabin.ter.constants.participant.constant.RocketMqSysConstant;
import com.cabin.ter.constants.participant.msg.RocketEnhanceProperties;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StringUtils;

/**
 * <p>
 *     RocketMQ 发送消息模板
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-05 15:31
 */

@Slf4j
@Data
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RocketMQEnhanceTemplate {
    private final RocketMQTemplate rocketMQTemplate;
    @Resource
    private RocketEnhanceProperties rocketEnhanceProperties;


    /**
     * 根据系统上下文自动构建隔离后的topic
     * 构建目的地
     */
    public String buildDestination(String topic, String tag) {
        topic = reBuildTopic(topic);
        return topic + RocketMqSysConstant.DELIMITER + tag;
    }
    /**
     * 根据环境重新隔离topic
     * @param topic 原始topic
     */
    private String reBuildTopic(String topic) {
        if(rocketEnhanceProperties.isEnabledIsolation() && StringUtils.hasText(rocketEnhanceProperties.getEnvironment())){
            return topic + RocketMqSysConstant.RETRY_DELIMITER + rocketEnhanceProperties.getEnvironment();
        }
        return topic;
    }
    /**
     * 发送同步消息
     */
    public <T extends MQBaseMessage> SendResult send(String topic, String tag, T message) {
        // 注意分隔符
        return send(buildDestination(topic,tag), message);
    }


    public <T extends MQBaseMessage> SendResult send(String destination, T message) {
        Message<T> sendMessage = MessageBuilder.withPayload(message).setHeader(RocketMQHeaders.KEYS, message.getKey()).build();
        SendResult sendResult = rocketMQTemplate.syncSend(destination, sendMessage);

        log.info("[{}]同步消息[{}]发送结果[{}]", destination, JSONObject.toJSON(message), JSONObject.toJSON(sendResult));
        return sendResult;
    }

    /**
     * 发送延迟消息
     */
    public <T extends MQBaseMessage> SendResult send(String topic, String tag, T message, int delayLevel) {
        return send(buildDestination(topic,tag), message, delayLevel);
    }

    public <T extends MQBaseMessage> SendResult send(String destination, T message, int delayLevel) {
        Message<T> sendMessage = MessageBuilder.withPayload(message).setHeader(RocketMQHeaders.KEYS, message.getKey()).build();
        SendResult sendResult = rocketMQTemplate.syncSend(destination, sendMessage, 3000, delayLevel);
        log.info("[{}]延迟等级[{}]消息[{}]发送结果[{}]", destination, delayLevel, JSONObject.toJSON(message), JSONObject.toJSON(sendResult));
        return sendResult;
    }
}

