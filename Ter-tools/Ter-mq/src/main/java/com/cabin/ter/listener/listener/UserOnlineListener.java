package com.cabin.ter.listener.listener;

import com.cabin.ter.listener.event.UserOnlineEvent;
import com.cabin.ter.cache.UserInfoCache;
import com.cabin.ter.vo.UserPrincipal;
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


    /**
     * 用户上线监听
     *
     * @param event
     */
    @Async(value = "terExecutor")
    @EventListener(classes = UserOnlineEvent.class)
    public void saveRedisAndPush(UserOnlineEvent event) {
        UserPrincipal user = event.getUserPrincipal();
        userCache.online(user.getUserId(), user.getLastOptTime());
    }
}
