package com.cabin.ter.common.controller;


import com.cabin.ter.common.payload.LoginRequest;
import com.cabin.ter.common.service.UserService;
import com.cabin.ter.constants.vo.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping(value = "/api/user",consumes  = "application/x-www-form-urlencoded")
@Tag(name = "用户管理模块")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "用户登录接口")
    @PostMapping("/login")
    public ApiResponse userLogin(@Valid @ModelAttribute LoginRequest loginRequest){
        return userService.userLogin(loginRequest);
    }

    @PostMapping("/register")
    public ApiResponse userRegister(@Valid @ModelAttribute LoginRequest loginRequest){
        return userService.userRegister(loginRequest);
    }
}
