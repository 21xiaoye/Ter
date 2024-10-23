package com.cabin.ter.controller;

import com.cabin.ter.constants.vo.response.ApiResponse;
import com.cabin.ter.service.ChatService;
import com.cabin.ter.util.RequestHolderUtil;
import com.cabin.ter.vo.request.ChatMessageReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author xiaoye
 * @date Created in 2024-05-29 10:54
 */
@RestController
@RequestMapping("/api/chat")
@Tag(name = "消息接口")
@Slf4j
public class ChatController {
    @Autowired
    private ChatService chatService;

    // TODO: 未来在这里添加频控注解，限制前端用户的消息发送
    @Operation(description = "发送消息接口")
    @PostMapping("/sendMsg")
    public ApiResponse senMsg(@Valid @RequestBody ChatMessageReq chatMessageReq){
        Long uid = RequestHolderUtil.get().getUid();
        log.info("收到用户{}信息{}",uid, chatMessageReq);
        Long msgId = chatService.sendMsg(chatMessageReq, uid);
        return ApiResponse.ofSuccess(chatService.getMsgResp(msgId));
    }
}
