package com.cabin.ter.listener.listener;

import com.cabin.ter.adapter.WebSocketMessageBuilderAdapter;
import com.cabin.ter.cache.GroupMemberCache;
import com.cabin.ter.cache.RoomCache;
import com.cabin.ter.cache.RoomFriendCache;
import com.cabin.ter.chat.domain.FriendRoomDomain;
import com.cabin.ter.chat.domain.RoomDomain;
import com.cabin.ter.constants.enums.RoomTypeEnum;
import com.cabin.ter.constants.TopicConstant;
import com.cabin.ter.constants.response.ChatMessageResp;
import com.cabin.ter.constants.response.WSBaseResp;
import com.cabin.ter.listener.event.MessageSendEvent;
import com.cabin.ter.template.RocketMQEnhanceTemplate;
import com.cabin.ter.adapter.MQMessageBuilderAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.*;

/**
 * <p>
 *     发送消息监听器
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-29 15:46
 */
@Component
@Slf4j
public class MessageSendListener {
    @Autowired
    private RocketMQEnhanceTemplate rocketMQEnhanceTemplate;
    @Autowired
    private RoomCache roomCache;
    @Autowired
    private GroupMemberCache groupMemberCache;
    @Autowired
    private RoomFriendCache roomFriendCache;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, classes = MessageSendEvent.class, fallbackExecution = true)
    @Async(value = "terExecutor")
    public void messageRoute(MessageSendEvent event) {
        List<Long> userList = null;
        ChatMessageResp chatMessageResp = event.getChatMessageResp();
        RoomDomain roomDomain = roomCache.get(chatMessageResp.getMessage().getRoomId());
        // 群聊则获取所有群聊成员
        if(Objects.equals(roomDomain.getRoomType(), RoomTypeEnum.GROUP.getType())){
            log.info("群聊消息，对群聊好友进行消息推送");
            userList = groupMemberCache.getMemberUidList(roomDomain.getRoomId());
        }
        // 单聊，对好友进行推送
        if(Objects.equals(roomDomain.getRoomType(), RoomTypeEnum.FRIEND.getType())){
            log.info("单聊信息，对好友进行推送");
            Map<Long, FriendRoomDomain> friendRoomDomainMap = roomFriendCache.getBatch(Collections.singletonList(roomDomain.getRoomId()));
            userList = new ArrayList<>(friendRoomDomainMap.keySet());
        }
        WSBaseResp<ChatMessageResp> chatMessageRespWSBaseResp = WebSocketMessageBuilderAdapter.buildChatMessage(chatMessageResp);
        rocketMQEnhanceTemplate.sendSecureMsg(TopicConstant.CHAT_MESSAGE_SEND_TOPIC, MQMessageBuilderAdapter.buildChatMessageDTO(chatMessageRespWSBaseResp, userList));
    }
}
