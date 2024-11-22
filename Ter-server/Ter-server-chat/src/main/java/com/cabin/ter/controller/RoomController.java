package com.cabin.ter.controller;

import com.cabin.ter.constants.response.ApiResponse;
import com.cabin.ter.service.RoomInfoService;
import com.cabin.ter.util.RequestHolderUtil;
import com.cabin.ter.constants.request.GroupAddReq;
import com.cabin.ter.constants.response.IdResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author xiaoye
 * @date Created in 2024-05-28 19:40
 */

@RestController
@RequestMapping("/api/group")
@Tag(name = "房间相关接口")
@Slf4j
public class RoomController {
    @Autowired
    private RoomInfoService roomInfoService;

    @PostMapping("/createGroup")
    @Operation(description = "新增群组")
    public ApiResponse addGroup(@Valid @RequestBody GroupAddReq groupAddReq){
        Long userId = RequestHolderUtil.get().getUserId();
        Long roomId = roomInfoService.addGroup(userId, groupAddReq);
        return ApiResponse.ofSuccess(IdResp.id(roomId));
    }
    @GetMapping("/groupInfo")
    @Operation(description = "获取群组信息")
    public ApiResponse getGroupInfo(
            @Schema(name = "roomId", description = "群组的roomId")
            @RequestParam Long roomId){
        return ApiResponse.ofSuccess(roomInfoService.getGroupInfo(roomId));
    }
    @PutMapping("/quit")
    @Operation(description = "退出群组")
    public ApiResponse quitGroup(
            @Schema(name = "rooId", description = "退出的群roomId")
            @RequestParam Long  roomId){
        Long userId = RequestHolderUtil.get().getUserId();
        roomInfoService.quitGroup(roomId, userId);
        return ApiResponse.ofSuccess();
    }
    @PutMapping("/modifyMemberRemark")
    @Operation(description = "修改成员群备注")
    public ApiResponse modifyMemberRemark(
            @Schema(name = "userId", description = "用户的userId")
            @RequestParam Long userId,

            @Schema(name = "roomId", description = "修改修改群备注的群roomId")
            @RequestParam Long roomId,

            @Schema(name = "memberRemark", description = "新的成员群备注")
            @RequestParam String memberRemark
    ){
        roomInfoService.modifyMemberRemark(userId, roomId, memberRemark);
        return ApiResponse.ofSuccess();
    }
    @PostMapping("/invite")
    @Operation(description = "邀请群成员")
    public ApiResponse inviteMember(@RequestBody GroupAddReq groupAddReq,

                                    @Schema(name = "roomId", description = "群组roomId")
                                    @RequestParam Long roomId){
        Long userId = RequestHolderUtil.get().getUserId();
        roomInfoService.inviteMember(roomId, userId ,groupAddReq);
        return ApiResponse.ofSuccess();
    }
    @PutMapping("/kick")
    @Operation(description = "踢出群成员")
    public ApiResponse kickMember(
            @Schema(name = "memberIdList", description = "需要提出的群成员userId")
            @RequestBody List<Long> memberIdList,

            @Schema(name = "roomId", description = "群roomId")
            @RequestBody Long roomId
            ){
        roomInfoService.kickMember(roomId, memberIdList);
        return ApiResponse.ofSuccess();
    }
    @PutMapping("/modifyMemberRole")
    @Operation(description = "设置群管理员")
    public ApiResponse modifyMemberRole(
            @Schema(name = "roomId", description = "群roomId")
            @RequestParam("roomId") Long roomId,

            @Schema(name = "modifyUserId", description = "被修改者的userId")
            @RequestParam Long modifyUserId,

            @Schema(name = "roleId", description = "群权限roleId 1群主 2管理员 3成员")
            @RequestParam Long roleId){

        Long userId = RequestHolderUtil.get().getUserId();
        roomInfoService.modifyMemberRole(roomId, userId, modifyUserId, roleId);
        return ApiResponse.ofSuccess();
    }
}
