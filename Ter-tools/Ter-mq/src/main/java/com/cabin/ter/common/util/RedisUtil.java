package com.cabin.ter.common.util;


import com.alibaba.fastjson.JSON;
import com.cabin.ter.common.constants.participant.TopicConstant;
import com.cabin.ter.common.constants.participant.ws.SendChannelInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     redis 发布者配置
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


    public void push(String channel, String message) {
        redisTemplate.convertAndSend(channel, message);
    }
}