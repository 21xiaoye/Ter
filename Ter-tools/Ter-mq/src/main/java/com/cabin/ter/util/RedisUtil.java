package com.cabin.ter.util;


import com.alibaba.fastjson.JSON;
import com.cabin.ter.constants.participant.constant.TopicConstant;
import com.cabin.ter.constants.participant.ws.SendChannelInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *     redis 配置
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-02 21:57
 */
@Slf4j
@Component
public class RedisUtil {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void pushObj(SendChannelInfo userChannelInfo) {
        redisTemplate.opsForHash().put(TopicConstant.REDIS_USER_MESSAGE_PUSH,
                userChannelInfo.getChannelId(), JSON.toJSONString(userChannelInfo));
    }

    public List<SendChannelInfo> popList() {
        List<Object> values = redisTemplate.opsForHash().values(TopicConstant.REDIS_USER_MESSAGE_PUSH);
        if (null == values) {
            return new ArrayList<>();
        }

        List<SendChannelInfo> userChannelInfoList = new ArrayList<>();

        for (Object strJson : values) {
            userChannelInfoList.add(JSON.parseObject(strJson.toString(), SendChannelInfo.class));
        }
        return userChannelInfoList;
    }

    public void remove(String channelId) {
        redisTemplate.opsForHash().delete(TopicConstant.REDIS_USER_MESSAGE_PUSH, channelId);
    }

    public void clear() {
        redisTemplate.delete(TopicConstant.REDIS_USER_MESSAGE_PUSH);
    }

    /**
     * 往redis 主体推送消息
     * @param channel
     * @param message
     */
    public void push(String channel, String message) {
        redisTemplate.convertAndSend(channel, message);
    }




    private static final String LUA_INCR_EXPIRE =
            "local key,ttl=KEYS[1],ARGV[1] \n" +
                    " \n" +
                    "if redis.call('EXISTS',key)==0 then   \n" +
                    "  redis.call('SETEX',key,ttl,1) \n" +
                    "  return 1 \n" +
                    "else \n" +
                    "  return tonumber(redis.call('INCR',key)) \n" +
                    "end ";
    public  Long inc(String key, int time, TimeUnit unit){
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
    public  String objToStr(Object o) {
        return JsonUtils.toStr(o);
    }

    public  Boolean set(String key, Object value, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, objToStr(value), time, timeUnit);
            } else {
                set(key, value);
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
    public  Long zCard(String key){
        return redisTemplate.opsForZSet().zCard(key);
    }
}