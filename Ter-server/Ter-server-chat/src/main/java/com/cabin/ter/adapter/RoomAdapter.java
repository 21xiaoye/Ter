package com.cabin.ter.adapter;

import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.chat.domain.GroupRoomDomain;
import com.cabin.ter.constants.enums.MessageTypeEnum;
import com.cabin.ter.vo.request.ChatMessageReq;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

public class RoomAdapter {
    public static ChatMessageReq buildGroupAddMessage(GroupRoomDomain groupRoom, UserDomain inviter, Map<Long, UserDomain> member) {
        ChatMessageReq chatMessageReq = new ChatMessageReq();
        chatMessageReq.setRoomId(groupRoom.getRoomId());
        chatMessageReq.setMessageType(MessageTypeEnum.SYSTEM.getStatus());
        StringBuilder sb = new StringBuilder();
        sb.append("\"")
                .append(inviter.getUserName())
                .append("\"")
                .append("邀请")
                .append(member.values().stream().map(u -> "\"" + u.getUserName() + "\"").collect(Collectors.joining(",")))
                .append("加入群聊");
        chatMessageReq.setBody(sb.toString());
        return chatMessageReq;
    }
}
