package com.cabin.ter.cache;

import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.admin.mapper.UserDomainMapper;
import com.cabin.ter.constants.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xiaoye
 * @date Created in 2024-05-28 20:48
 */
@Component
public class UserInfoCache extends AbstractRedisStringCache<Long, UserDomain> {
    @Autowired
    private UserDomainMapper userDomainMapper;

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
        return needLoadUserList.stream().collect(Collectors.toMap(UserDomain::getUId, Function.identity()));
    }
}
