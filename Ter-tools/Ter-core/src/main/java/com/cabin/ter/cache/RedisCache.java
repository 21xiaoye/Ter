package com.cabin.ter.cache;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *     redis 缓存工具类
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-08 16:38
 */
@Slf4j
@Component
public class RedisCache {

    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String LUA_INCR_EXPIRE =
            "local key,ttl=KEYS[1],ARGV[1] \n" +
                    " \n" +
                    "if redis.call('EXISTS',key)==0 then   \n" +
                    "  redis.call('SETEX',key,ttl,1) \n" +
                    "  return 1 \n" +
                    "else \n" +
                    "  return tonumber(redis.call('INCR',key)) \n" +
                    "end ";
    public Long inc(String key, int time, TimeUnit unit){
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(LUA_INCR_EXPIRE, Long.class);
        return redisTemplate.execute(redisScript, Collections.singletonList(key), String.valueOf(unit.toSeconds(time)));
    }

    public  Integer integerInc(String key, int time, TimeUnit unit){
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(LUA_INCR_EXPIRE, Long.class);
        Long result = redisTemplate.execute(redisScript, Collections.singletonList(key), String.valueOf(unit.toSeconds(time)));
        try {
            return Integer.parseInt(result.toString());
        }catch (Exception e){
            del(key );
            throw e;
        }
    }

    /**
     * 删除缓存
     * @param keys
     */
    public void del(String... keys){
        if(keys !=null && keys.length > 0){
            if(keys.length == 1){
                Boolean result = redisTemplate.delete(keys[0]);
                log.debug("---------------------------------------");
                log.debug("删除缓存："+keys[0]+".结束"+result);
            }else {
                HashSet<String> keyset = new HashSet<>();
                for (String key : keyset){
                    Set<String> keysed = redisTemplate.keys(key);
                    if(Objects.nonNull(keysed) && !keysed.isEmpty()){
                        keyset.addAll(keysed);
                    }
                }
                Long count = redisTemplate.delete(keyset);
                log.debug("-----------------------------------------");
                log.debug("成功删除缓存："+keyset);
                log.debug("删除缓存数量："+count);
            }
            log.debug("---------------------------------------------");
        }
    }
}
