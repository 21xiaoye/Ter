package com.cabin.ter.controller;

import com.cabin.ter.constants.response.ApiResponse;
import com.cabin.ter.service.RoomInfoService;
import com.cabin.ter.util.RequestHolderUtil;
import com.cabin.ter.constants.request.GroupAddReq;
import com.cabin.ter.constants.response.IdRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author xiaoye
 * @date Created in 2024-05-28 19:40
 */

@RestController
@RequestMapping("/api/room")
@Tag(name = "房间相关接口")
@Slf4j
public class RoomController {
    @Autowired
    private RoomInfoService roomInfoService;

    @PostMapping("/group")
    @Operation(description = "新增群组")
    public ApiResponse addGroup(@Valid @RequestBody GroupAddReq groupAddReq){
        Long uId = RequestHolderUtil.get().getUid();
        Long roomId = roomInfoService.addGroup(uId, groupAddReq);
        return ApiResponse.ofSuccess(IdRespVO.id(roomId));
    }
























}
