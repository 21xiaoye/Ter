package com.cabin.ter.listener.listener;

import com.cabin.ter.adapter.MQMessageBuilderAdapter;
import com.cabin.ter.constants.TopicConstant;
import com.cabin.ter.constants.enums.SourceEnum;
import com.cabin.ter.listener.event.UserOnlineEvent;
import com.cabin.ter.cache.UserInfoCache;
import com.cabin.ter.template.RocketMQEnhanceTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * <p>
 *     用户上线监听器
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-27 17:12
 */
@Slf4j
@Component
public class UserOnlineListener {
    @Autowired
    private UserInfoCache userCache;
    @Autowired
    private RocketMQEnhanceTemplate rocketMQEnhanceTemplate;
    @Async(value = "terExecutor")
    @EventListener(classes = UserOnlineEvent.class)
    public void saveRedisAndPush(UserOnlineEvent event) {
        Long userId = event.getUserId();
        Long onlineTime = event.getOnlineTime();
        userCache.online(userId, onlineTime);
        // 发送上线通知
        rocketMQEnhanceTemplate.send(TopicConstant.GLOBAL_USER_ONLINE_TOPIC, MQMessageBuilderAdapter.buildUserOnlineNotifyDTO(userId, onlineTime));
    }
}
