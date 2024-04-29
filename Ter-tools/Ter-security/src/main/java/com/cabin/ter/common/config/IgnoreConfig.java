package com.cabin.ter.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *  自定义配置
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-04-19 16:47
 */
@Configuration
@ConfigurationProperties(prefix = "customer.config")
@Data
public class IgnoreConfig {
}
