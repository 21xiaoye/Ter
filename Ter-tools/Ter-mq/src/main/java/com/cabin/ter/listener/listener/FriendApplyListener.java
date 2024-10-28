package com.cabin.ter.listener.listener;

import com.cabin.ter.adapter.WebSocketMessageBuilderAdapter;
import com.cabin.ter.constants.vo.response.WSApplyUserInfoResp;
import com.cabin.ter.listener.event.FriendApplyEvent;
import com.cabin.ter.service.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class FriendApplyListener {
    @Autowired
    private PushService pushService;
    @Async
    @TransactionalEventListener(classes = FriendApplyEvent.class, fallbackExecution = true)
    public void notifyFriend(FriendApplyEvent event) {
        log.info("推送好友申请事件......");
        pushService.sendPushMsg(WebSocketMessageBuilderAdapter.buildApplyUserInfoResp(new WSApplyUserInfoResp(event.getUserId())), event.getTargetId());
    }
}
