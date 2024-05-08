package com.cabin.ter.config;

import com.cabin.ter.constants.participant.msg.RocketEnhanceProperties;
import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.StringUtils;


/**
 * <p>
 *     环境隔离配置
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-05 15:45
 */
public class EnvironmentIsolationConfig implements BeanPostProcessor {
    private RocketEnhanceProperties rocketEnhanceProperties;

    public EnvironmentIsolationConfig(RocketEnhanceProperties rocketEnhanceProperties) {
        this.rocketEnhanceProperties = rocketEnhanceProperties;
    }


    /**
     * 在装载Bean之前实现参数修改
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof DefaultRocketMQListenerContainer){

            DefaultRocketMQListenerContainer container = (DefaultRocketMQListenerContainer) bean;

            if(rocketEnhanceProperties.isEnabledIsolation() && StringUtils.hasText(rocketEnhanceProperties.getEnvironment())){
                container.setTopic(String.join("_", container.getTopic(),rocketEnhanceProperties.getEnvironment()));
            }
            return container;
        }
        return bean;
    }
}
