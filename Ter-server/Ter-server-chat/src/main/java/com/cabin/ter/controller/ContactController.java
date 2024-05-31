package com.cabin.ter.controller;

import com.cabin.ter.cache.RoomCache;
import com.cabin.ter.constants.vo.response.ApiResponse;
import com.cabin.ter.dao.ContactDomainDao;
import com.cabin.ter.service.RoomInfoService;
import com.cabin.ter.util.RequestHolderUtil;
import com.cabin.ter.constants.vo.request.CursorPageBaseReq;
import com.cabin.ter.vo.response.ChatRoomResp;
import com.cabin.ter.constants.vo.response.CursorPageBaseResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;

@RestController
@RequestMapping("/api/contact")
@Tag(name = "会话接口")
@Slf4j
public class ContactController {
    @Autowired
    private RoomCache roomCache;
    @Autowired
    private RoomInfoService roomInfoService;
    @Autowired
    private ContactDomainDao contactDomainDao;
    @GetMapping("/page")
    @Operation(description = "获取会话列表接口")
    public ApiResponse getRoomPage(@Valid CursorPageBaseReq cursorPageBaseReq){
        return ApiResponse.ofSuccess(contactDomainDao.getByRoomIds(Arrays.asList(1L,2L), RequestHolderUtil.get().getUid()));
//        Long uid = RequestHolderUtil.get().getUid();
//        return ApiResponse.ofSuccess(roomInfoService.getContactPage(cursorPageBaseReq, uid));
    }
}
