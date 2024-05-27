package com.cabin.ter.service.cache;

import com.cabin.ter.cache.RedisCache;
import com.cabin.ter.constants.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author xiaoye
 * @date Created in 2024-05-27 14:40
 */

@Component
public class UserCache {
    @Autowired
    private RedisCache redisCache;
    /**
     * 获取在线在线人数
     */
    public Long LongOnlineNum(){
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZET);
        return redisCache.zCard(onlineKey);
    }

    /**
     * 移除用户
     *
     * @param uid
     */

    public void remote(Long uid){
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZET);
        String offlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZET);

        redisCache.zRemove(offlineKey, uid);
        redisCache.zRemove(onlineKey, uid);
    }

    /**
     * 用户上线
     *
     * @param uid
     * @param optTime
     */
    public void online(Long uid, Date optTime){
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZET);
        String offlineKey = RedisKey.getKey(RedisKey.OFFLINE_UID_ZET);

        redisCache.zRemove(offlineKey, uid);
        redisCache.zAdd(onlineKey, uid, optTime.getTime());
    }

    /**
     * 用户下线
     *
     * @param uid
     * @param optTime
     */
    public void offline(Long uid, Long optTime) {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZET);
        String offlineKey = RedisKey.getKey(RedisKey.OFFLINE_UID_ZET);
        //移除上线线表
        redisCache.zRemove(onlineKey, uid);
        //更新上线表
        redisCache.zAdd(offlineKey, uid, optTime);
    }

    public boolean isOnline(Long uid) {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZET);
        return redisCache.zIsMember(onlineKey, uid);
    }

}





























