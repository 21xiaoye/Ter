package com.cabin.ter.listener;

import com.cabin.ter.adapter.MQMessageBuilderAdapter;
import com.cabin.ter.adapter.WebSocketMessageBuilderAdapter;
import com.cabin.ter.constants.TopicConstant;
import com.cabin.ter.listener.event.FriendApplyEvent;
import com.cabin.ter.template.RocketMQEnhanceTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Collections;

@Component
@Slf4j
public class FriendApplyListener {
    @Autowired
    private RocketMQEnhanceTemplate rocketMQEnhanceTemplate;
    @Async
    @TransactionalEventListener(classes = FriendApplyEvent.class, fallbackExecution = true)
    public void notifyFriend(FriendApplyEvent event) {
        log.info("推送好友申请事件......");
        rocketMQEnhanceTemplate.sendSecureMsg(TopicConstant.CHAT_MESSAGE_SEND_TOPIC,
                MQMessageBuilderAdapter.buildChatMessageDTO(WebSocketMessageBuilderAdapter.
                        buildApplyUserInfoResp(event.getFriendApplyResp()), Collections.singletonList(event.getTargetId())));
    }
}
