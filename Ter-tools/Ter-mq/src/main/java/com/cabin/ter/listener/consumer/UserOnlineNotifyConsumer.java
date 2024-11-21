package com.cabin.ter.listener.consumer;

import com.cabin.ter.adapter.WebSocketMessageBuilderAdapter;
import com.cabin.ter.cache.RoomFriendCache;
import com.cabin.ter.constants.TopicConstant;
import com.cabin.ter.constants.dto.UserOnlineNotifyDTO;
import com.cabin.ter.constants.participant.constant.ConsumerNameConstant;
import com.cabin.ter.listener.BaseMqMessageListener;
import com.cabin.ter.service.WebSocketPublicService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户离线消息通知消费者
 * 当用户离线通知用户好友，更改用户在线状态
 *
 * @author xiaoye
 * @date Created in 2024/11/4 16:23
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = TopicConstant.GLOBAL_USER_ONLINE_TOPIC,
        consumerGroup = TopicConstant.GLOBAL_USER_ONLINE_GROUP,
        messageModel = MessageModel.BROADCASTING,
        consumeMode = ConsumeMode.CONCURRENTLY,
        consumeThreadNumber = 5
)
public class UserOnlineNotifyConsumer extends BaseMqMessageListener<UserOnlineNotifyDTO>
        implements RocketMQListener<UserOnlineNotifyDTO> {
    @Autowired
    private RoomFriendCache roomFriendCache;
    @Autowired
    private WebSocketPublicService webSocketPublicService;
    @Override
    protected void handleMessage(UserOnlineNotifyDTO message) {
        log.info("对用户进行上线通知");
        List<Long> userFriendId = roomFriendCache.getUserFriendId(message.getUserId());
        userFriendId.forEach(userId->{
            webSocketPublicService.sendToUid(WebSocketMessageBuilderAdapter.buildUserOnlineResp(userId), userId);
        });
    }

    @Override
    protected void handleMaxRetriesExceeded(UserOnlineNotifyDTO message) {
        log.info("进行上线通知事务补偿{}",message);
    }

    @Override
    protected String ConsumerName() {
        return ConsumerNameConstant.USER_ONLINE_NOTIFY_CONSUMER;
    }

    @Override
    protected boolean isRetry() {
        return false;
    }

    @Override
    protected boolean throwException() {
        return false;
    }

    @Override
    public void onMessage(UserOnlineNotifyDTO userOnlineNotifyDTO) {
        super.dispatchMessage(userOnlineNotifyDTO);
    }
}
