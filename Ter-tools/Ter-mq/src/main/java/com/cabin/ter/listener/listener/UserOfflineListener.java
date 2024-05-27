package com.cabin.ter.listener.listener;

import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.listener.event.UserOfflineEvent;
import com.cabin.ter.service.cache.UserCache;
import com.cabin.ter.vo.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

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
    private UserCache userCache;
    // TODO: 这里在用户下线之后做一些操作，我这里有些犹豫是将用户的状态记录到mysql还是记录到redis当中

    @Async(value = "terExecutor")
    @EventListener(classes = UserOfflineEvent.class)
    public void saveRedisAndPush(UserOfflineEvent userOfflineEvent){
        UserDomain userDomain = userOfflineEvent.getUserDomain();
        log.info("用户下线监听执行{}",userDomain);
        userCache.offline(userDomain.getUId(), userDomain.getLastOptTime());
    }

    @Async(value = "terExecutor")
    @EventListener(classes = UserOfflineEvent.class)
    public void saveDB(UserOfflineEvent userOfflineEvent){
        log.info("保存用户离线状态");
    }
}
