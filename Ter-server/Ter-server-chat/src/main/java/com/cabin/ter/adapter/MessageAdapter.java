package com.cabin.ter.adapter;

import cn.hutool.core.lang.Snowflake;
import com.cabin.ter.chat.domain.MessageDomain;
import com.cabin.ter.chat.enums.MessageStatusEnum;
import com.cabin.ter.vo.request.ChatMessageReq;
import com.cabin.ter.vo.response.ChatMessageResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xiaoye
 * @date Created in 2024-05-29 15:10
 */
@Component
public class MessageAdapter {
    @Autowired
    private Snowflake snowflake;
    public MessageDomain buildMsgSave(ChatMessageReq request, Long uid){
        return MessageDomain.builder()
                .id(snowflake.nextId())
                .fromUid(uid)
                .roomId(request.getRoomId())
                .type(request.getMessageType())
                .status(MessageStatusEnum.NORMAL.getStatus())
                .createTime(System.currentTimeMillis())
                .build();
    }
}
