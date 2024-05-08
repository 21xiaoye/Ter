package com.cabin.ter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * <p>
 * JWT 配置
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-04-19 16:40
 */
@ConfigurationProperties(prefix = "jwt.config")
@Data
public class JwtConfig {
    /**
     * jwt 加密 key，默认值：ter_coding.
     */
    private String key = "ter_coding";

    /**
     * jwt 过期时间，默认值：600000 {@code 10 分钟}.
     */
    private Long ttl = 600000L;

    /**
     * 开启 记住我 之后 jwt 过期时间，默认值 604800000 {@code 7 天}
     */
    private Long remember = 604800000L;
}
