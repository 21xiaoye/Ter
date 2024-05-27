package com.cabin.ter.cache;

import com.cabin.ter.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.security.core.parameters.P;
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

    public Long UIDInc(String key){
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(LUA_INCR_EXPIRE, Long.class);
        return redisTemplate.execute(redisScript, Collections.singletonList(key));
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

    /**
     * 普通缓存放入并设置时间
     *
     * @param key      键
     * @param value    值
     * @param time     时间
     * @param timeUnit 类型
     * @return true成功 false 失败
     */
    public  Boolean set(String key, Object value, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, objToStr(value), time, timeUnit);
            } else {
                this.set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public  Boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, objToStr(value));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
    public Boolean setHashMap(String key,String hashKey, Object value){
        try {
            redisTemplate.opsForHash().put(key, hashKey, objToStr(value));
            return true;
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return false;
        }
    }

    public void delHashMap(String key, String... hashKey){
        redisTemplate.opsForHash().delete(key,hashKey);
    }
    public Object getHashMap(String key, String hashKey){
        if(key == null || hashKey == null){
            return null;
        }
        return redisTemplate.opsForHash().get(key,hashKey);
    }

    public static String objToStr(Object o) {
        return JsonUtils.toStr(o);
    }
    private  String get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    public  <T> T get(String key, Class<T> tClass) {
        String s = get(key);
        return toBeanOrNull(s, tClass);
    }

    static <T> T toBeanOrNull(String json, Class<T> tClass) {
        return json == null ? null : JsonUtils.toObj(json, tClass);
    }

    public  Boolean zAdd(String key, String value, Long score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    public  Boolean zAdd(String key, Object value, Long score) {
        return zAdd(key, value.toString(), score);
    }

    public Boolean zIsMember(String key, Object value) {
        return Objects.nonNull(redisTemplate.opsForZSet().score(key, value.toString()));
    }


    public  Long zCard(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }

    public Long zRemove(String key, Object value) {
        return zRemove(key, value.toString());
    }

    public  Long zRemove(String key, String value) {
        return redisTemplate.opsForZSet().remove(key, value);
    }
}
