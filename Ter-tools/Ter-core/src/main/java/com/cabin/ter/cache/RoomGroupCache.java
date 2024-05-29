package com.cabin.ter.cache;

import com.cabin.ter.chat.domain.GroupRoomDomain;
import com.cabin.ter.chat.mapper.GroupRoomDomainMapper;
import com.cabin.ter.constants.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xiaoye
 * @date Created in 2024-05-29 13:37
 */
@Component
public class RoomGroupCache extends AbstractRedisStringCache<Long, GroupRoomDomain> {
    @Autowired
    private GroupRoomDomainMapper groupRoomDomainMapper;

    @Override
    protected String getKey(Long roomId) {
        return RedisKey.getKey(RedisKey.GROUP_INFO_STRING, roomId);
    }

    @Override
    protected Long getExpireSeconds() {
        return 5 * 60L;
    }

    @Override
    protected Map<Long, GroupRoomDomain> load(List<Long> req) {
        List<GroupRoomDomain> groupRoomDomainList = groupRoomDomainMapper.listByIds(req);
        return groupRoomDomainList.stream().collect(Collectors.toMap(GroupRoomDomain::getRoomId, Function.identity()));
    }
}
