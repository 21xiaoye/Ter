package com.cabin.ter.service;

import com.cabin.ter.vo.request.ChatMessageReq;
import com.cabin.ter.vo.response.ChatMessageResp;

/**
 * @author xiaoye
 * @date Created in 2024-05-29 11:00
 */
public interface ChatService {
    /**
     * 发送消息
     *
     * @param chatMessageReq    消息请求体
     * @param uid               用户uid
     * @return  消息id
     */
    Long senMsg(ChatMessageReq chatMessageReq, Long uid);

    /**
     * 获取消息响应
     *
     * @param msgId         消息id
     * @param receiveUid    接受用户uid
     * @return  ChatMessageResp 消息响应体
     */
    ChatMessageResp getMsgResp(Long msgId, Long receiveUid);
}
