package com.cabin.ter.config;

import com.cabin.ter.constants.participant.msg.RocketEnhanceProperties;
import com.cabin.ter.template.RocketMQEnhanceTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;

import java.util.List;

/**
 * <p>
 *     RocketMQ 消息队列配置
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-04 00:42
 */
@Configuration
@Import({RocketMQAutoConfiguration.class})
@EnableConfigurationProperties(RocketEnhanceProperties.class)
public class RocketMQConfig  {
    @Bean
    public RocketMQEnhanceTemplate rocketMQEnhanceTemplate(RocketMQTemplate rocketMQTemplate){
        return new RocketMQEnhanceTemplate(rocketMQTemplate);
    }

    /**
     * 转换 Cannot construct instance of java.time.LocalDate 错误
     * RocketMQ 内置转换JSON的转换器，不支持Java LocalDate/LocalDateTime的时间类型
     *
     * 替换MappingJackson2MessageConverter转换器，增加时间转换模块
     * @return
     */
    @Bean
    @Primary
    public RocketMQMessageConverter createRocketMqMessageConverter(){
        RocketMQMessageConverter converter = new RocketMQMessageConverter();
        CompositeMessageConverter compositeMessageConverter = (CompositeMessageConverter) converter.getMessageConverter();
        List<MessageConverter> messageConverterList = compositeMessageConverter.getConverters();
        for (MessageConverter messageConverter : messageConverterList) {
            if (messageConverter instanceof MappingJackson2MessageConverter) {
                MappingJackson2MessageConverter jackson2MessageConverter = (MappingJackson2MessageConverter) messageConverter;
                ObjectMapper objectMapper = jackson2MessageConverter.getObjectMapper();
                // 增加Java8时间模块支持，实体类可以传递LocalDate/LocalDateTime
                objectMapper.registerModules(new JavaTimeModule());
            }
        }
        return converter;
    }

    /**
     * 环境隔离配置
     */
    @Bean
    @ConditionalOnProperty(name="rocketmq.enhance.enabledIsolation", havingValue="true")
    public EnvironmentIsolationConfig environmentSetup(RocketEnhanceProperties rocketEnhanceProperties){
        return new EnvironmentIsolationConfig(rocketEnhanceProperties);
    }
}























