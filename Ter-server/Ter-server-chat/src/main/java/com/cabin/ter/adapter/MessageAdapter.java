package com.cabin.ter.adapter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import com.cabin.ter.chat.domain.MessageDomain;
import com.cabin.ter.chat.enums.MessageStatusEnum;
import com.cabin.ter.constants.enums.WSRespTypeEnum;
import com.cabin.ter.constants.vo.response.WSBaseResp;
import com.cabin.ter.strategy.AbstractMsgHandler;
import com.cabin.ter.strategy.MsgHandlerFactory;
import com.cabin.ter.vo.request.ChatMessageReq;
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

    private  ChatMessageResp.UserInfo buildFromUser(Long fromUid) {
        ChatMessageResp.UserInfo userInfo = new ChatMessageResp.UserInfo();
        userInfo.setUid(fromUid);
        return userInfo;
    }

    public WSBaseResp<ChatMessageResp> buildMsgSend(ChatMessageResp msgResp) {
        WSBaseResp<ChatMessageResp> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.MESSAGE.getType());
        wsBaseResp.setData(msgResp);
        return wsBaseResp;
    }

}
