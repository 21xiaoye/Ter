package com.cabin.ter.common.constants.participant.msg;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 *     rocketMQ 环境隔离
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-05 17:39
 */
@ConfigurationProperties(prefix = "rocketmq.enhance")
@Data
public class RocketEnhanceProperties {

    private boolean enabledIsolation;

    private String environment;
}