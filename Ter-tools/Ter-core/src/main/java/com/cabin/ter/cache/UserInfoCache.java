package com.cabin.ter.cache;

import cn.hutool.core.collection.CollUtil;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.admin.mapper.UserDomainMapper;
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
public class UserInfoCache extends AbstractRedisStringCache<Long, UserDomain>{
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
     * 用户上线
     *
     * @param uId
     * @param optTime
     */
    public void online(Long uId, Long optTime){
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZET);
        String offlineKey = RedisKey.getKey(RedisKey.OFFLINE_UID_ZET);

        redisCache.zRemove(offlineKey, uId);
        redisCache.zRemove(onlineKey, uId);
        redisCache.zAdd(onlineKey, uId, optTime);
    }

    /**
     * 用户下线
     *
     * @param uId
     * @param optTime
     */
    public void offline(Long uId, Long optTime) {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZET);
        String offlineKey = RedisKey.getKey(RedisKey.OFFLINE_UID_ZET);
        //移除上线线表
        redisCache.zRemove(offlineKey, uId);
        redisCache.zRemove(onlineKey, uId);
        //更新上线表
        redisCache.zAdd(offlineKey, uId, optTime);
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
    private Map<Long, UserDomain> getUserInfoBatch(Set<Long> uIds) {
        //批量组装key
        List<String> keys = uIds.stream().map(a -> RedisKey.getKey(RedisKey.USER_ONLINE_INFO, a)).collect(Collectors.toList());
        //批量get
        List<UserDomain> userDomainsCache = redisCache.mget(keys, UserDomain.class);
        Map<Long, UserDomain> map = userDomainsCache.stream().filter(Objects::nonNull).collect(Collectors.toMap(UserDomain::getUserId, Function.identity()));
        //发现差集——还需要load更新的uid
        List<Long> needLoadUidList = uIds.stream().filter(a -> !map.containsKey(a)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(needLoadUidList)) {
            //批量load
            List<UserDomain> needLoadUserList = userDomainMapper.listByIds(needLoadUidList);
            Map<String, UserDomain> redisMap = needLoadUserList.stream().collect(Collectors.toMap(a -> RedisKey.getKey(RedisKey.USER_ONLINE_INFO, a.getUserId()), Function.identity()));
            redisCache.mset(redisMap, 5 * 60);
            //加载回redis
            map.putAll(needLoadUserList.stream().collect(Collectors.toMap(UserDomain::getUserId, Function.identity())));
        }
        return map;
    }
    public UserDomain getUserInfoBatch(String email){
        UserDomain userDomain = userDomainMapper.findByUserEmail(email);
        if (Objects.nonNull(userDomain)) {
            redisCache.mset(RedisKey.getKey(RedisKey.USER_ONLINE_INFO, userDomain.getUserId()), userDomain,5*60);
        }
        return userDomain;
    }

    @Override
    protected String getKey(Long uid) {
        return RedisKey.getKey(RedisKey.USER_ONLINE_INFO, uid);
    }

    @Override
    protected Long getExpireSeconds() {
        return 5 * 60L;
    }

    @Override
    protected Map<Long, UserDomain> load(List<Long> uidList) {
        List<UserDomain> needLoadUserList = userDomainMapper.listByIds(uidList);
        return needLoadUserList.stream().collect(Collectors.toMap(UserDomain::getUserId, Function.identity()));
    }
}