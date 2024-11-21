package com.cabin.ter.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.cabin.ter.adapter.MessageAdapter;
import com.cabin.ter.cache.GroupMemberCache;
import com.cabin.ter.cache.MessageCache;
import com.cabin.ter.cache.RoomCache;
import com.cabin.ter.chat.domain.MessageDomain;
import com.cabin.ter.chat.domain.RoomDomain;
import com.cabin.ter.listener.event.MessageSendEvent;
import com.cabin.ter.service.ChatService;
import com.cabin.ter.strategy.AbstractMsgHandler;
import com.cabin.ter.strategy.MsgHandlerFactory;
import com.cabin.ter.util.AsserUtil;
import com.cabin.ter.constants.request.ChatMessageReq;
import com.cabin.ter.constants.response.ChatMessageResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *     会话服务层
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-29 11:01
 */
@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private RoomCache roomCache;
    @Autowired
    private MessageCache messageCache;
    @Autowired
    private GroupMemberCache groupMemberCache;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public Long sendMsg(ChatMessageReq chatMessageReq, Long userId) {
        this.roomCheck(chatMessageReq, userId);
        AbstractMsgHandler abstractMsgHandler = MsgHandlerFactory.getStrategyNoNull(chatMessageReq.getMessageType());
        Long msgId = abstractMsgHandler.checkAndSaveMsg(chatMessageReq, userId);
        applicationEventPublisher.publishEvent(new MessageSendEvent(this, getMsgResp(msgId)));
        return msgId;
    }

    @Override
    public ChatMessageResp getMsgResp(MessageDomain message) {
        return CollUtil.getFirst(getMsgRespBatch(Collections.singletonList(message)));
    }

    @Override
    public ChatMessageResp getMsgResp(Long msgId) {
        MessageDomain messageDomain = messageCache.getMsg(msgId);
        return this.getMsgResp(messageDomain);
    }

    private void roomCheck(ChatMessageReq chatMessageReq, Long uid){
        RoomDomain roomDomain = roomCache.get(chatMessageReq.getRoomId());
        AsserUtil.isEmpty(roomDomain, "该群不存在");
        if(roomDomain.isHotRoom()){
            return;
        }
        if(roomDomain.isRoomGroup()){
            // 查询群组成员
            List<Long> uidList = groupMemberCache.getMemberUidList(chatMessageReq.getRoomId());
            AsserUtil.isEmpty(uidList, "该群已解散");
            if(!uidList.contains(uid)){
                AsserUtil.isEmpty(null, "您已经被移除该群");
            }
        }
    }

    public List<ChatMessageResp> getMsgRespBatch(List<MessageDomain> messages) {
        if (CollectionUtil.isEmpty(messages)) {
            return new ArrayList<>();
        }
        return MessageAdapter.buildMsgResp(messages);
    }
}
