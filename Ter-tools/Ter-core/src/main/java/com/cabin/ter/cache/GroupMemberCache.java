package com.cabin.ter.cache;


import com.cabin.ter.chat.domain.GroupMemberDomain;
import com.cabin.ter.chat.domain.GroupRoomDomain;
import com.cabin.ter.chat.domain.RoomDomain;
import com.cabin.ter.chat.mapper.GroupMemberDomainMapper;
import com.cabin.ter.chat.mapper.GroupRoomDomainMapper;
import com.cabin.ter.constants.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author xiaoye
 * @date Created in 2024-05-29 13:49
 */
@Component
public class GroupMemberCache extends AbstractRedisStringCache<Long, GroupMemberDomain> {
    @Autowired
    private GroupMemberDomainMapper groupMemberDomainMapper;
    @Autowired
    private RoomGroupCache roomGroupCache;

    /**
     * 这里是根据群组 id 拿到群成员 id
     *
     * 如果本地没有，就去redis缓存中拿
     * @param roomId
     * @return
     */

    @Cacheable(cacheNames = "member", key = "'groupMember'+#roomId")
    public List<Long> getMemberUidList(Long roomId) {
        // 没有就去 redis 缓存中拿, redis 缓存中还没有会自动去数据库中查询
        GroupRoomDomain groupRoomDomain = roomGroupCache.get(roomId);

        if(Objects.isNull(groupRoomDomain)){
            return null;
        }

        return groupMemberDomainMapper.getMemberUidList(groupRoomDomain.getRoomId());
    }

    @CacheEvict(cacheNames = "member", key = "'groupMember'+#roomId")
    public List<Long> evictMemberUidList(Long roomId) {
        return null;
    }

    @Override
    protected String getKey(Long roomId) {
        return RedisKey.getKey(RedisKey.GROUP_MEMBER_INFO_STRING, roomId);
    }

    @Override
    protected Long getExpireSeconds() {
        return 5*60L;
    }

    @Override
    protected Map<Long, GroupMemberDomain> load(List<Long> req) {
        List<GroupMemberDomain> groupMemberDomains = groupMemberDomainMapper.listByIds(req);
        return groupMemberDomains.stream().collect(Collectors.toMap(GroupMemberDomain::getGroupId, Function.identity()));
    }
}
