package com.cabin.ter.controller;

import com.cabin.ter.constants.response.ApiResponse;
import com.cabin.ter.service.RoomInfoService;
import com.cabin.ter.util.RequestHolderUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
@Tag(name = "会话接口")
@Slf4j
public class ContactController {
    @Autowired
    private RoomInfoService roomInfoService;
    @GetMapping("/page")
    @Operation(description = "获取会话列表接口")
    public ApiResponse getRoomPage(){
        return ApiResponse.ofSuccess(roomInfoService.getUserContactPage(RequestHolderUtil.get().getUserId()));
    }
}
