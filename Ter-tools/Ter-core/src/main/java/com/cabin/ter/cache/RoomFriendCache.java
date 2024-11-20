package com.cabin.ter.cache;

import com.cabin.ter.chat.domain.FriendRoomDomain;
import com.cabin.ter.chat.mapper.FriendRoomDomainMapper;
import com.cabin.ter.constants.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RoomFriendCache extends AbstractRedisStringCache<Long, FriendRoomDomain> {
    @Autowired
    private FriendRoomDomainMapper friendRoomDomainMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    protected String getKey(Long req) {
        return RedisKey.getKey(RedisKey.GROUP_INFO_STRING, req);
    }

    @Override
    protected Long getExpireSeconds() {
        return 5 * 60L;
    }

    @Override
    protected Map<Long, FriendRoomDomain> load(List<Long> req) {
        List<FriendRoomDomain> listedByRoomIds = friendRoomDomainMapper.listByIds(req);
        return listedByRoomIds.stream().collect(Collectors.toMap(FriendRoomDomain::getRoomId, Function.identity()));
    }

    /**
     * 获取正常关系的好友信息列表
     * @param userId    用户userId
     * @return  返回好友信息列表
     */
    public List<FriendRoomDomain> getUserFriendInfoSet(Long userId) {
        Set<FriendRoomDomain> setValue = redisCache.getSetValue(RedisKey.getKey(RedisKey.USER_FRIEND, userId), FriendRoomDomain.class);

        if (setValue.isEmpty()) {
            List<FriendRoomDomain> friendPage = friendRoomDomainMapper.getFriendPage(userId, FriendRoomDomain.FRIENDSHIP_RECOVER);
            redisCache.setAdd(RedisKey.getKey(RedisKey.USER_FRIEND, userId), friendPage);
            return new ArrayList<>(friendPage);
        }

        return new ArrayList<>(setValue);
    }

    /**
     * 获取用户好友userId列表
     * @param userId    用户userId
     * @return  返回用户好友userId列表
     */
    public List<Long> getUserFriendId(Long userId) {
        List<FriendRoomDomain> userFriendInfoSet = getUserFriendInfoSet(userId);

        return userFriendInfoSet.stream()
                .map(FriendRoomDomain::getFriendId)
                .collect(Collectors.toList());
    }

}
