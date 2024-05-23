package com.cabin.ter.controller;


import com.cabin.ter.constants.vo.request.EmailBindingReqMsg;
import com.cabin.ter.constants.vo.request.LoginAndRegisterRequest;
import com.cabin.ter.service.UserService;
import com.cabin.ter.constants.vo.response.ApiResponse;

import com.cabin.ter.util.AsserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

/**
 * @author xiaoye
 * @date Created in 2024-05-22
 */
@RestController
@Slf4j
@RequestMapping(value = "/api/common")
@Tag(name = "公共模块")
public class CommonController {
    @Autowired
    private UserService userService;

    @Operation(summary = "用户登录接口")
    @PostMapping("/login")
    public ApiResponse userLogin(@Valid @RequestBody LoginAndRegisterRequest loginRequest){
        return userService.userLogin(loginRequest);
    }

    @Operation(summary = "用户注册接口")
    @PostMapping("/register")
    public ApiResponse userRegister(@Valid @RequestBody LoginAndRegisterRequest loginRequest){
        return userService.userRegister(loginRequest);
    }

    @Operation(summary = "绑定邮箱接口")
    @PostMapping("/emailBinding")
    public ApiResponse emailBinding(@Valid @RequestBody EmailBindingReqMsg emailBindingReqMsg){
        log.info("openId=[{}]绑定邮箱email=[{}]",emailBindingReqMsg.getOpenId(),emailBindingReqMsg.getEmail());
        return userService.emailBiding(emailBindingReqMsg);
    }
    @Operation(summary = "发送邮箱验证码接口")
    @GetMapping("/sendEmailCode")
    public ApiResponse sendEmailCode(
            @Valid @ModelAttribute EmailBindingReqMsg emailBindingReqMsg) {
        AsserUtil.fastFailValidate(emailBindingReqMsg);
        log.info("email=[{}]发送邮箱验证码",emailBindingReqMsg.getEmail());
        return userService.sendEmailCode(emailBindingReqMsg);
    }
}
