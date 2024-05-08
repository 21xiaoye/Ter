package com.cabin.ter.service;

import com.cabin.ter.constants.participant.msg.MessageParticipant;
import com.cabin.ter.constants.enums.MessagePushMethodEnum;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * <p>
 *     消息工厂
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-01 22:04
 */
@Component
public class MessageStrategyServiceFactory implements ApplicationContextAware{
    @Autowired
    private ListableBeanFactory beanFactory;
    private static final Object lock = new Object();
    private volatile  static Map<MessagePushMethodEnum, BaseMessageStrategyService> MAP;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (Objects.isNull(MAP)) {
            synchronized (lock) {
                if (Objects.isNull(MAP)) {
                    MAP = new HashMap<>();
                    Map<String, BaseMessageStrategyService> beansOfType = beanFactory.getBeansOfType(BaseMessageStrategyService.class);
                    beansOfType.values().forEach(strategy -> MAP.put(strategy.getSource(), strategy));
                }
            }
        }
    }

    /**
     * 获取消息策略
     *
     * @param messageType 消息策略类型参数
     * @return  消息策略
     */
    public static BaseMessageStrategyService getStrategy(MessagePushMethodEnum messageType) {
        return MAP.get(messageType);
    }


    /**
     * 对外暴露的工厂方法,直接执行消息推送
     *
     * @param message   消息
     * @param messageType   消息策略类型参数
     * @return  Boolean
     */
    public Boolean getAwardResult(MessageParticipant message, MessagePushMethodEnum messageType) {
        try {
            BaseMessageStrategyService strategy = getStrategy(messageType);
            if (Objects.isNull(strategy)) {
                throw new RuntimeException("获取消息策略异常,策略加载失败");
            }
            return strategy.messageStrategy(message);
        }catch (Exception e){
            throw new RuntimeException("获取消息策略异常"+e);
        }
    }

    /**
     * 创建单例工厂对象
     */
    private static class CreateFactorySingleton{
        private static MessageStrategyServiceFactory messageStrategyServiceFactory = new MessageStrategyServiceFactory();

    }

    public static MessageStrategyServiceFactory getInstance(){
        return CreateFactorySingleton.messageStrategyServiceFactory;
    }
}
