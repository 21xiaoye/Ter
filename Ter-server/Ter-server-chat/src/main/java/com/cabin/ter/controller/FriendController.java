package com.cabin.ter.controller;

import com.cabin.ter.constants.vo.response.ApiResponse;
import com.cabin.ter.service.FriendService;
import com.cabin.ter.util.RequestHolderUtil;
import com.cabin.ter.vo.request.ApprovalFriendReq;
import com.cabin.ter.vo.request.FriendApplyReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user/friend")
@Tag(name = "好友服务接口")
public class FriendController {
    @Autowired
    private FriendService friendService;
    @Operation(summary = "好友申请接口")
    @PostMapping("/apply")
    public ApiResponse friendApply(@Valid @RequestBody FriendApplyReq friendApplyReq){
        Long uid = RequestHolderUtil.get().getUid();
        friendService.apply(uid,friendApplyReq);
        return ApiResponse.ofSuccess();
    }

    @GetMapping("/apply/page")
    @Operation(summary = "好友申请列表")
    public ApiResponse friendApplyPage() {
        Long uId = RequestHolderUtil.get().getUid();
        return ApiResponse.ofSuccess(friendService.getFriendApplyRecord(uId));
    }

    @PostMapping("/apply/approval")
    @Operation(summary = "审批好友申请")
    public ApiResponse approvalFriendApplyRecord(@RequestBody ApprovalFriendReq approvalFriendReq){
        friendService.operateFriendApplyRecord(approvalFriendReq,RequestHolderUtil.get().getUid());
        return ApiResponse.ofSuccess();
    }
    @DeleteMapping("/apply/friendApply")
    @Operation(summary = "删除好友申请")
    public ApiResponse deleteFriendApply(@RequestBody ApprovalFriendReq approvalFriendReq){
        friendService.operateFriendApplyRecord(approvalFriendReq, RequestHolderUtil.get().getUid());
        return ApiResponse.ofSuccess();
    }
}
