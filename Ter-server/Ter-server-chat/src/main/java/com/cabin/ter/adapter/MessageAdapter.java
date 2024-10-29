package com.cabin.ter.adapter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.chat.domain.GroupRoomDomain;
import com.cabin.ter.chat.domain.MessageDomain;
import com.cabin.ter.chat.enums.MessageStatusEnum;
import com.cabin.ter.constants.enums.MessageTypeEnum;
import com.cabin.ter.constants.enums.WSRespTypeEnum;
import com.cabin.ter.constants.vo.response.WSBaseResp;
import com.cabin.ter.strategy.AbstractMsgHandler;
import com.cabin.ter.strategy.MsgHandlerFactory;
import com.cabin.ter.vo.request.ChatMessageReq;
import com.cabin.ter.vo.request.TextMsgReq;
import com.cabin.ter.vo.response.ChatMessageResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaoye
 * @date Created in 2024-05-29 15:10
 */
@Component
public class MessageAdapter {
    public static final int CAN_CALLBACK_GAP_COUNT = 100;
    @Autowired
    private Snowflake snowflake;
    /**
     * 构建一条消息持久化实体对象
     *
     * @param request   消息请求对象
     * @param uid   发送者uId
     * @return  返回消息持久化实体对象
     */
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

    /**
     * 构建消息响应对象
     * @param messages  需要响应消息实体对象列表
     * @return  消息响应对象列表
     */
    public  List<ChatMessageResp> buildMsgResp(List<MessageDomain> messages) {
        return messages.stream().map(a -> {
                    ChatMessageResp resp = new ChatMessageResp();
                    resp.setFromUser(this.buildFromUser(a.getFromUid()));
                    resp.setMessage(this.buildMessage(a));
                    return resp;
                })
                .sorted(Comparator.comparing(a -> a.getMessage().getSendTime()))//帮前端排好序，更方便它展示
                .collect(Collectors.toList());
    }

    /**
     * 构建消息响应对象中消息对象信息
     *
     * @param message   需要响应的消息实体对象
     * @return  返回构建消息响应对象中消息对象信息
     */
    private  ChatMessageResp.Message buildMessage(MessageDomain message) {
        ChatMessageResp.Message messageVO = new ChatMessageResp.Message();
        BeanUtil.copyProperties(message, messageVO);
        messageVO.setSendTime(message.getCreateTime());
        AbstractMsgHandler<?> msgHandler = MsgHandlerFactory.getStrategyNoNull(message.getType());
        if (Objects.nonNull(msgHandler)) {
            messageVO.setBody(msgHandler.showMsg(message));
        }
        return messageVO;
    }
    /**
     * 构建消息响应对象中发送者对象信息
     *
     * @param fromUid   消息发送者uId
     * @return  返回消息响应对象那个中发送者的对象信息
     */
    private  ChatMessageResp.UserInfo buildFromUser(Long fromUid) {
        ChatMessageResp.UserInfo userInfo = new ChatMessageResp.UserInfo();
        userInfo.setUid(fromUid);
        return userInfo;
    }

    /**
     * 构建群组创建的邀请信息实体对象
     *
     * @param groupRoom 群组信息
     * @param inviter   创建者信息
     * @param member    邀请成员信息
     * @return  返回响应邀请信息实体对象
     */
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

    /**
     * 构建WebSocket推送给前端的响应对象
     *
     * @param msgResp   需要推送给前端的消息数据
     * @return  返回WebSocket推送响应对象
     */
    public WSBaseResp<ChatMessageResp> buildMsgSend(ChatMessageResp msgResp) {
        WSBaseResp<ChatMessageResp> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.MESSAGE.getType());
        wsBaseResp.setData(msgResp);
        return wsBaseResp;
    }

    public static ChatMessageReq buildAgreeMsg(Long roomId) {
        ChatMessageReq chatMessageReq = new ChatMessageReq();
        chatMessageReq.setRoomId(roomId);
        chatMessageReq.setMessageType(MessageTypeEnum.TEXT.getStatus());
        TextMsgReq textMsgReq = new TextMsgReq();
        textMsgReq.setContent("我们已经成为好友了，开始聊天吧");
        chatMessageReq.setBody(textMsgReq);
        return chatMessageReq;
    }
}
