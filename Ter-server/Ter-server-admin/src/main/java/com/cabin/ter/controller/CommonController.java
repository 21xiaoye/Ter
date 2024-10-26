package com.cabin.ter.controller;


import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.constants.enums.Status;
import com.cabin.ter.vo.request.LoginAndRegisterRequest;
import com.cabin.ter.service.UserService;
import com.cabin.ter.constants.vo.response.ApiResponse;

import com.cabin.ter.util.AsserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

/**
 * @author xiaoye
 * @date Created in 2024-05-22
 */
@RestController
@Slf4j
@RequestMapping(value = "/api/common/user")
@Tag(name = "公共模块")
public class CommonController {
    @Autowired
    private UserService userService;
    @Operation(summary = "邮箱验证码接口")
    @GetMapping("/sendVerificationCode")
    public ApiResponse sendVerificationCode(
            @RequestParam
            @Email
            @Schema(name = "userEmail",description = "目标邮箱地址")
            String userEmail,
            @RequestParam
            @Schema(name = "operationType", description = "邮件用途 1001:用户注册 1003:用户验证码登录")
            Integer operationType) {
        userService.sendMailCode(userEmail, operationType);
        return ApiResponse.ofSuccess("验证码发送......");
    }

    @Operation(summary = "用户登录接口",description = "operationType参数 账户密码登录1002,验证码登录1003")
    @PostMapping("/login")
    public ApiResponse userLogin(@Valid @RequestBody LoginAndRegisterRequest loginRequest){
        AsserUtil.fastFailValidate(loginRequest);
        return userService.userLogin(loginRequest);
    }
    @Operation(summary = "用户注册接口")
    @PostMapping("/register")
    public ApiResponse userRegister(@Valid @RequestBody LoginAndRegisterRequest loginRequest){
        AsserUtil.fastFailValidate(loginRequest);
        UserDomain userDomain = userService.userRegister(loginRequest);
        AsserUtil.isEmpty(userDomain, "注册失败");
        userService.saveUser(userDomain);
        return ApiResponse.ofStatus(Status.SUCCESS);
    }


}
