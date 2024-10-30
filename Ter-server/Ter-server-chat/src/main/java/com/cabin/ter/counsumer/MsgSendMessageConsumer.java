package com.cabin.ter.counsumer;

import com.cabin.ter.adapter.MessageAdapter;
import com.cabin.ter.cache.GroupMemberCache;
import com.cabin.ter.cache.MessageCache;
import com.cabin.ter.cache.RoomCache;
import com.cabin.ter.chat.domain.MessageDomain;
import com.cabin.ter.chat.domain.RoomDomain;
import com.cabin.ter.chat.enums.RoomTypeEnum;
import com.cabin.ter.constants.dto.MsgSendMessageDTO;
import com.cabin.ter.constants.participant.constant.ConsumerNameConstant;
import com.cabin.ter.constants.TopicConstant;
import com.cabin.ter.listener.BaseMqMessageListener;
import com.cabin.ter.service.ChatService;
import com.cabin.ter.service.PushService;
import com.cabin.ter.vo.response.ChatMessageResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author xiaouye
 * @date Created in 2024-05-29 18:31
 */
@Slf4j
@RocketMQMessageListener(consumerGroup = TopicConstant.SEND_MSG_GROUP, topic = TopicConstant.SEND_MSG_TOPIC)
@Component
public class MsgSendMessageConsumer extends BaseMqMessageListener<MsgSendMessageDTO> implements RocketMQListener<MsgSendMessageDTO> {
    @Autowired
    private MessageCache messageCache;
    @Autowired
    private RoomCache roomCache;
    @Autowired
    private ChatService chatService;
    @Autowired
    private GroupMemberCache groupMemberCache;
    @Autowired
    private PushService pushService;
    @Override
    public void onMessage(MsgSendMessageDTO msgSendMessageDTO) {
        super.dispatchMessage(msgSendMessageDTO);
    }

    @Override
    protected void handleMessage(MsgSendMessageDTO message) {
        MessageDomain messageDomain = messageCache.getMsg(message.getMsgId());
        RoomDomain roomDomain = roomCache.get(messageDomain.getRoomId());
        ChatMessageResp msgResp = chatService.getMsgResp(messageDomain);

        if(roomDomain.isHotRoom()){
            pushService.sendPushMsg(MessageAdapter.buildMsgSend(msgResp));
        }else{
            // 群聊则获取所有群聊成员
            if(Objects.equals(roomDomain.getRoomType(), RoomTypeEnum.GROUP.getType())){
                List<Long> memberUidList = groupMemberCache.getMemberUidList(roomDomain.getRoomId());
                pushService.sendPushMsg(MessageAdapter.buildMsgSend(msgResp), memberUidList);
            }
            // 单聊，对好友进行推送
            if(Objects.equals(roomDomain.getRoomType(), RoomTypeEnum.FRIEND.getType())){
                log.info("单聊信息，对好友进行推送");
            }
        }
    }

    @Override
    protected void handleMaxRetriesExceeded(MsgSendMessageDTO message) {
        log.info("{}消息消费失败了",message);
    }

    @Override
    protected String ConsumerName() {
        return ConsumerNameConstant.MSG_SEND_MESSAGE_CONSUMER;
    }

    @Override
    protected boolean isRetry() {
        return true;
    }

    @Override
    protected boolean throwException() {
        return false;
    }
}
