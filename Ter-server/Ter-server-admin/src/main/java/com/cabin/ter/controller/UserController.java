package com.cabin.ter.controller;


import com.cabin.ter.constants.vo.request.EmailBindingReqMsg;
import com.cabin.ter.constants.vo.request.LoginAndRegisterRequest;
import com.cabin.ter.service.UserService;
import com.cabin.ter.constants.vo.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
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
@RequestMapping(value = "/api/user",consumes  = "application/x-www-form-urlencoded")
@Tag(name = "用户管理模块")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "用户登录接口")
    @PostMapping("/login")
    public ApiResponse userLogin(@Valid @ModelAttribute LoginAndRegisterRequest loginRequest){
        return userService.userLogin(loginRequest);
    }

    @Operation(summary = "用户注册接口")
    @PostMapping("/register")
    public ApiResponse userRegister(@Valid @ModelAttribute LoginAndRegisterRequest loginRequest){
        return userService.userRegister(loginRequest);
    }

    @Operation(summary = "邮箱验证码验证接口")
    @PostMapping("/emailVerify")
    public ApiResponse emailBinding(@Valid @ModelAttribute EmailBindingReqMsg emailBindingReqMsg){
        log.info("openId=[{}]绑定邮箱email=[{}]",emailBindingReqMsg.getOpenId(),emailBindingReqMsg.getEmail());
        return userService.emailVerify(emailBindingReqMsg);
    }
    @Operation(summary = "发送邮箱验证码接口")
    @PostMapping("/sendEmailCode")
    public ApiResponse sendEmailCode(@Valid @ModelAttribute EmailBindingReqMsg emailBindingReqMsg){
        log.info("email=[{}]发送邮箱验证码",emailBindingReqMsg.getEmail());
        return userService.sendEmailCode(emailBindingReqMsg);
    }
}
