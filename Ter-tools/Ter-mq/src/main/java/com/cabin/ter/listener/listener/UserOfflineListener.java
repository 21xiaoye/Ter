package com.cabin.ter.listener.listener;

import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.listener.event.UserOfflineEvent;
import com.cabin.ter.cache.UserInfoCache;
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
    @Async(value = "terExecutor")
    @EventListener(classes = UserOfflineEvent.class)
    public void saveRedisAndPush(UserOfflineEvent userOfflineEvent){
        UserDomain userDomain = userOfflineEvent.getUserDomain();
        log.info("用户下线监听执行{}",userDomain);
        userCache.offline(userDomain.getUserId(), userDomain.getLastOptTime());
    }

    @Async(value = "terExecutor")
    @EventListener(classes = UserOfflineEvent.class)
    public void saveDB(UserOfflineEvent userOfflineEvent){
        log.info("保存用户离线状态");
    }
}
