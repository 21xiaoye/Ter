package com.cabin.ter.listener.listener;

import com.cabin.ter.adapter.MQMessageBuilderAdapter;
import com.cabin.ter.constants.TopicConstant;
import com.cabin.ter.constants.enums.SourceEnum;
import com.cabin.ter.listener.event.UserOfflineEvent;
import com.cabin.ter.cache.UserInfoCache;
import com.cabin.ter.template.RocketMQEnhanceTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 用户下线监听器
 *
 * @author xiaoye
 * @date Created in 2024-05-27 17:39
 */
@Slf4j
@Component
public class UserOfflineListener {
    @Autowired
    private UserInfoCache userCache;
    @Autowired
    private RocketMQEnhanceTemplate rocketMQEnhanceTemplate;
    @Async(value = "terExecutor")
    @EventListener(classes = UserOfflineEvent.class)
    public void saveRedisAndPush(UserOfflineEvent event){
        Long userId = event.getUserId();
        Long offlineTime = event.getOfflineTime();
        log.info("用户下线监听执行{}",userId);
        userCache.offline(userId, offlineTime);
        rocketMQEnhanceTemplate.send(TopicConstant.GLOBAL_USER_OFFLINE_TOPIC, MQMessageBuilderAdapter.buildUserOfflineNotifyDTO(userId, offlineTime, SourceEnum.USER_OFFLINE_SOURCE));
    }

    @Async(value = "terExecutor")
    @EventListener(classes = UserOfflineEvent.class)
    public void saveDB(UserOfflineEvent userOfflineEvent){
        log.info("保存用户离线状态");
    }
}
