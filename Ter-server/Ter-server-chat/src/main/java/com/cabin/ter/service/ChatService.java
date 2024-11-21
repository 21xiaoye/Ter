package com.cabin.ter.service;

import com.cabin.ter.chat.domain.MessageDomain;
import com.cabin.ter.constants.request.ChatMessageReq;
import com.cabin.ter.constants.response.ChatMessageResp;

/**
 * @author xiaoye
 * @date Created in 2024-05-29 11:00
 */
public interface ChatService {
    /**
     * 发送消息
     *
     * @param chatMessageReq    消息请求体
     * @param userId            用户userId
     * @return  消息id
     */
    Long sendMsg(ChatMessageReq chatMessageReq, Long userId);

    /**
     * 获取消息响应
     *
     * @param msgId         消息id
     * @return  ChatMessageResp 消息响应体
     */
    ChatMessageResp getMsgResp(Long msgId);

    ChatMessageResp getMsgResp(MessageDomain message);
}
