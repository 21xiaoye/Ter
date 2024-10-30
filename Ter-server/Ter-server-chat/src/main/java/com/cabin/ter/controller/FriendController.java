package com.cabin.ter.controller;

import com.cabin.ter.chat.domain.FriendRoomDomain;
import com.cabin.ter.constants.vo.response.ApiResponse;
import com.cabin.ter.service.FriendService;
import com.cabin.ter.util.RequestHolderUtil;
import com.cabin.ter.vo.request.ApprovalFriendReq;
import com.cabin.ter.vo.request.FriendApplyReq;
import com.cabin.ter.vo.request.WhiteReq;
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
        friendService.agreeFriendApply(approvalFriendReq,RequestHolderUtil.get().getUid());
        return ApiResponse.ofSuccess();
    }
    @DeleteMapping("/apply/friendApply")
    @Operation(summary = "删除好友申请")
    public ApiResponse deleteFriendApply(@RequestBody ApprovalFriendReq approvalFriendReq){
        friendService.agreeFriendApply(approvalFriendReq, RequestHolderUtil.get().getUid());
        return ApiResponse.ofSuccess();
    }
    @PutMapping("/blockFriend")
    @Operation(summary = "拉黑好友")
    public ApiResponse blockFriend(@RequestParam Long friendId){
        friendService.operateFriendStatus(RequestHolderUtil.get().getUid(), friendId, FriendRoomDomain.FRIENDSHIP_BLOCK);
        return ApiResponse.ofSuccess();
    }
    @DeleteMapping("/deleteFriend")
    @Operation(summary = "删除好友")
    public ApiResponse deleteFriend(@RequestParam Long friendId){
        friendService.operateFriendStatus(RequestHolderUtil.get().getUid(), friendId, FriendRoomDomain.FRIENDSHIP_DELETE);
        return ApiResponse.ofSuccess();
    }
    @GetMapping("/page")
    @Operation(summary = "获取好友列表")
    public ApiResponse friendPage(){
        return ApiResponse.ofSuccess(friendService.getFriendPage(RequestHolderUtil.get().getUid()));
    }
    @GetMapping("/blockPage")
    @Operation(summary = "获取用户所有黑名单")
    public ApiResponse blockFriendPage(){
        return ApiResponse.ofSuccess(friendService.getBlockFriendPage(RequestHolderUtil.get().getUid()));
    }
    @PostMapping("/whitePage")
    @Operation(summary = "拉回白名单")
    public ApiResponse whiteFriendPage(@RequestBody WhiteReq whiteReq){
        friendService.pullBackWhitePage(RequestHolderUtil.get().getUid(), whiteReq);
        return ApiResponse.ofSuccess();
    }
}
