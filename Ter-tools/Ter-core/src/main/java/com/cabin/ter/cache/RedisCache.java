package com.cabin.ter.cache;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *     redis 缓存工具类
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-08 16:38
 */
public class RedisCache {
    private RedisTemplate<Object,Object> redisTemplate;

    public RedisTemplate<Object, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public void set(String key, Object value, long timeout){
        redisTemplate.opsForValue().set(key,value,timeout, TimeUnit.MILLISECONDS);
    }

    public void set(String key,Object value){
        redisTemplate.opsForValue().set(key,value);
    }

    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void hashAddAll(String key, Map<String, JSONObject> map){
        redisTemplate.opsForHash().putAll(key,map);
    }

    public void hashAdd(String key,String filed, Object value){
        redisTemplate.opsForHash().put(key,filed,value);
    }


    public Object hashSelectOne(String key,String filed){
        return redisTemplate.opsForHash().get(key,filed);
    }

    public Map<Object,Object> hashSelectAll(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }

    public void deleteHash(String key, String filed){
        redisTemplate.opsForHash().delete(key,filed);
    }

    public Long setAdd(String key,Object value){
        return redisTemplate.opsForSet().add(key,value);
    }

    public Set getSetData(String key){
        return redisTemplate.opsForSet().members(key);
    }

    public boolean isValueDateSet(String key, Object value){
        return redisTemplate.opsForSet().isMember(key,value);
    }

    public Set interSect(String keyOne,String keyTwo){
        return redisTemplate.opsForSet().intersect(keyOne,keyTwo);
    }

    public Set unionSet(String keyOne,String keyTwo){
        return redisTemplate.opsForSet().union(keyOne,keyTwo);
    }

    public void listAdd(String key, String value){
        redisTemplate.opsForList().leftPush(key,value);
    }

    public String listGet(String key){
        return (String) redisTemplate.opsForList().rightPop(key);
    }

    public void listAddObject(String key, Object object){
        redisTemplate.opsForList().leftPush(key,object);
    }

    public Object listGetObjet(String key){
        return redisTemplate.opsForList().rightPop(key);
    }

    public long getSerialNumber(String key, long value){
        return redisTemplate.opsForValue().increment(key,value);
    }

    public long getSurplusNumber(String key,String hashKey,long value){
        return redisTemplate.opsForHash().increment(key, hashKey, value);
    }
    public void hashPutSurplusNumber(String key,String filed,Long value){
        redisTemplate.opsForHash().put(key, filed, value.toString());
    }

    public Object getSurplusNumber(String key,String filed){
        return  redisTemplate.opsForHash().get(key, filed);
    }

    public long incr(String key,long expire){
        return redisTemplate.opsForValue().increment(key,expire);
    }

    public Set<String> keys(String key){
        Set keys = redisTemplate.keys(key + "*");
        return new HashSet<>(keys);
    }

    public List multiGet(List keyList){
        return redisTemplate.opsForValue().multiGet(keyList);
    }
}
