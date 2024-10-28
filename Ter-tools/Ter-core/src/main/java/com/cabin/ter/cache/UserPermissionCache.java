package com.cabin.ter.cache;

import com.cabin.ter.admin.domain.PermissionDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *     用户权限缓存
 * </p>
 *
 * @author xiaoye
 * @data Created in 2024/10/28 8:20
 */
@Component
public class UserPermissionCache extends AbstractRedisStringCache<Long, PermissionDomain> {
    @Autowired
    private RedisCache redisCache;

    @Override
    protected String getKey(Long req) {
        return null;
    }

    @Override
    protected Long getExpireSeconds() {
        return null;
    }

    @Override
    protected Map<Long, PermissionDomain> load(List<Long> req) {
        return null;
    }
}
