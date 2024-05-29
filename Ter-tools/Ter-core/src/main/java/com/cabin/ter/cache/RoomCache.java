package com.cabin.ter.cache;

import com.cabin.ter.chat.domain.RoomDomain;
import com.cabin.ter.chat.mapper.RoomDomainMapper;
import com.cabin.ter.constants.RedisKey;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *     房间缓存
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-29 13:06
 */
@Component
public class RoomCache extends AbstractRedisStringCache<Long, RoomDomain> {
    @Autowired
    private RoomDomainMapper roomDomainMapper;
    @Override
    protected String getKey(Long roomId) {
        return RedisKey.getKey(RedisKey.ROOM_INFO_STRING, roomId);
    }

    @Override
    protected Long getExpireSeconds() {
        return 5 * 60L;
    }

    @Override
    protected Map<Long, RoomDomain> load(List<Long> req) {
        List<RoomDomain> roomDomainList = roomDomainMapper.listByIds(req);
        return roomDomainList.stream().collect(Collectors.toMap(RoomDomain::getId, Function.identity()));
    }
}
