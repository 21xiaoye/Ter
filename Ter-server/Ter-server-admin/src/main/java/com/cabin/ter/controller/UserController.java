package com.cabin.ter.controller;


import com.cabin.ter.constants.domain.OssReq;
import com.cabin.ter.constants.vo.response.ApiResponse;

import com.cabin.ter.service.UserService;
import com.cabin.ter.util.RequestHolderUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 *     用户接口
 * </p>
 * @author xiaoye
 * @date Created in 2024-04-23 14:45
 */

@RestController
@Slf4j
@RequestMapping(value = "/api/user")
@Tag(name = "用户管理模块")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping(value = "/avatar")
    public ApiResponse uploadFile(@RequestBody OssReq ossReq){
        return userService.uploadAvatar(ossReq);
    }
    @GetMapping("/userInfo")
    @Operation(summary = "用户详情")
    public ApiResponse getUserInfo() {
        log.info("收到用户id{}的请求", RequestHolderUtil.get().getUid());
        return ApiResponse.ofSuccess(userService.getUserInfo(RequestHolderUtil.get().getUid()));
    }
    @PostMapping("/logOut")
    @Operation(summary = "退出登录")
    public void logOut(){
        userService.userLogOut(RequestHolderUtil.get().getUid());
    }
}
