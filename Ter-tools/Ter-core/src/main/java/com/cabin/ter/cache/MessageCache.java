package com.cabin.ter.cache;

import com.cabin.ter.chat.domain.MessageDomain;
import com.cabin.ter.chat.mapper.MessageDomainMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * <p>
 *     用户消息缓存
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-30 08:51
 */
@Component
@Slf4j
public class MessageCache {
    @Autowired
    private MessageDomainMapper messageDomainMapper;

    @Cacheable(cacheNames = "msg", key = "'msg'+#msgId")
    public MessageDomain getMsg(Long msgId) {
        return messageDomainMapper.getByMsgId(msgId);
    }

    @CacheEvict(cacheNames = "msg", key = "'msg'+#msgId")
    public MessageDomain evictMsg(Long msgId) {
        return null;
    }

}
