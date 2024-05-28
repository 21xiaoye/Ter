package com.cabin.ter.service.cache;

import cn.hutool.core.collection.CollUtil;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.admin.mapper.UserDomainMapper;
import com.cabin.ter.cache.RedisCache;
import com.cabin.ter.constants.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xiaoye
 * @date Created in 2024-05-27 14:40
 */

@Component
public class UserCache {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private UserDomainMapper userDomainMapper;
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

    public UserDomain getUserInfo(Long uid){
        return getUserInfoBatch(Collections.singleton(uid)).get(uid);
    }

    /**
     * 获取用户信息，盘路缓存模式
     */
    public Map<Long, UserDomain> getUserInfoBatch(Set<Long> uids) {
        //批量组装key
        List<String> keys = uids.stream().map(a -> RedisKey.getKey(RedisKey.USER_ONLINE_INFO, a)).collect(Collectors.toList());
        //批量get
        List<UserDomain> mget = redisCache.mget(keys, UserDomain.class);
        Map<Long, UserDomain> map = mget.stream().filter(Objects::nonNull).collect(Collectors.toMap(UserDomain::getUId, Function.identity()));
        //发现差集——还需要load更新的uid
        List<Long> needLoadUidList = uids.stream().filter(a -> !map.containsKey(a)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(needLoadUidList)) {
            //批量load
            List<UserDomain> needLoadUserList = userDomainMapper.listByIds(needLoadUidList);
            Map<String, UserDomain> redisMap = needLoadUserList.stream().collect(Collectors.toMap(a -> RedisKey.getKey(RedisKey.USER_ONLINE_INFO, a.getUId()), Function.identity()));
            redisCache.mset(redisMap, 5 * 60);
            //加载回redis
            map.putAll(needLoadUserList.stream().collect(Collectors.toMap(UserDomain::getUId, Function.identity())));
        }
        return map;
    }

}





























