package com.cabin.ter.cache;

import com.cabin.ter.chat.domain.FriendRoomDomain;
import com.cabin.ter.chat.mapper.FriendRoomDomainMapper;
import com.cabin.ter.constants.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RoomFriendCache extends AbstractRedisStringCache<Long, FriendRoomDomain> {
    @Autowired
    private FriendRoomDomainMapper friendRoomDomainMapper;

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
}
