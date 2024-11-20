package com.cabin.ter.util;


import com.alibaba.fastjson.JSON;
import com.cabin.ter.cache.RedisCache;
import com.cabin.ter.constants.TopicConstant;
import com.cabin.ter.constants.participant.ws.SendChannelInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.*;

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
    private RedisCache redisCache;
    private StringRedisTemplate redisTemplate;

    public void pushObj(SendChannelInfo userChannelInfo) {
        redisTemplate.opsForHash().put(TopicConstant.REDIS_GLOBAL_USER_LINE_STATUS,
                userChannelInfo.getChannelId(), JSON.toJSONString(userChannelInfo));
    }

    public List<SendChannelInfo> popList() {
        List<Object> values = redisTemplate.opsForHash().values(TopicConstant.REDIS_GLOBAL_USER_LINE_STATUS);

        List<SendChannelInfo> userChannelInfoList = new ArrayList<>();

        for (Object strJson : values) {
            userChannelInfoList.add(JSON.parseObject(strJson.toString(), SendChannelInfo.class));
        }
        return userChannelInfoList;
    }

    public void remove(String channelId) {
        redisTemplate.opsForHash().delete(TopicConstant.REDIS_GLOBAL_USER_LINE_STATUS, channelId);
    }

    public void clear() {
        redisTemplate.delete(TopicConstant.REDIS_GLOBAL_USER_LINE_STATUS);
    }

    /**
     * 往redis 主体推送消息
     * @param topicConstant
     * @param message
     */
    public void push(String topicConstant, String message) {
        redisCache.publishMessage(topicConstant, message);
    }
}