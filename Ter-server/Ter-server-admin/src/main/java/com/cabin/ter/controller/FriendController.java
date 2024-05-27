package com.cabin.ter.controller;

import com.cabin.ter.constants.vo.response.ApiResponse;
import com.cabin.ter.service.FriendService;
import com.cabin.ter.util.RequestHolderUtil;
import com.cabin.ter.vo.response.FriendApplyReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user/friend")
@Tag(name = "好友服务接口")
public class FriendController {
    @Autowired
    private FriendService friendService;
    @Operation(summary = "好友申请接口")
    @PutMapping("/apply")
    public ApiResponse friendApply(@Valid @RequestBody FriendApplyReq friendApplyReq){
        Long uid = RequestHolderUtil.get().getUid();
        friendService.apply(uid,friendApplyReq);
        return ApiResponse.ofSuccess();
    }
}
