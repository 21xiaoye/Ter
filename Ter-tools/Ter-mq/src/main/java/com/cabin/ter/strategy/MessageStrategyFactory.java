package com.cabin.ter.strategy;

import com.cabin.ter.constants.dto.MQBaseMessage;
import com.cabin.ter.constants.enums.MessagePushMethodEnum;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
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
public class MessageStrategyFactory implements ApplicationContextAware{
    @Autowired
    private ListableBeanFactory beanFactory;
    private static final Object lock = new Object();
    private volatile  static Map<MessagePushMethodEnum, MessageStrategyBase<?>> MAP;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        if (Objects.isNull(MAP)) {
            synchronized (lock) {
                if (Objects.isNull(MAP)) {
                    MAP = new HashMap<>();
                    Map<String, ?> beansOfType = beanFactory.getBeansOfType(MessageStrategyBase.class);
                    beansOfType.values().forEach(strategy -> MAP.put(((MessageStrategyBase<?>) strategy).getSource(), (MessageStrategyBase<?>) strategy));
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
    public static MessageStrategyBase<?> getStrategy(MessagePushMethodEnum messageType) {
        return MAP.get(messageType);
    }


    /**
     * 对外暴露的工厂方法,直接执行消息推送
     *
     * @param message   消息
     * @param messageType   消息策略类型参数
     * @return  Boolean
     */
    public void getAwardResult(MQBaseMessage message, MessagePushMethodEnum messageType) {
        try {
            MessageStrategyBase<?> strategy = getStrategy(messageType);
            if (Objects.isNull(strategy)) {
                throw new RuntimeException("获取消息策略异常,策略加载失败");
            }
            @SuppressWarnings("unchecked")
            MessageStrategyBase<MQBaseMessage> castedStrategy = (MessageStrategyBase<MQBaseMessage>) strategy;
            castedStrategy.messageStrategy(message);
        } catch (Exception e) {
            throw new RuntimeException("获取消息策略异常" + e);
        }
    }



    /**
     * 创建单例工厂对象
     */
    private static class CreateFactorySingleton{
        private static final MessageStrategyFactory messageStrategyServiceFactory = new MessageStrategyFactory();

    }

    public static MessageStrategyFactory getInstance(){
        return CreateFactorySingleton.messageStrategyServiceFactory;
    }
}
