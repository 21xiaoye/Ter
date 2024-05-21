package com.cabin.ter.service;

import com.cabin.ter.constants.vo.request.EmailBindingReqMsg;
import com.cabin.ter.constants.vo.request.LoginAndRegisterRequest;
import com.cabin.ter.constants.vo.response.ApiResponse;

/**
 * @author xiaoye
 * @date Created in 2024-04-23 14:46
 */
public interface UserService {
    /**
     * 用户登录
     *
     * @param loginRequest
     * @return
     */
    ApiResponse userLogin(LoginAndRegisterRequest loginRequest);

    /**
     * 用户注册
     * @param loginRequest
     * @return
     */
    ApiResponse userRegister(LoginAndRegisterRequest loginRequest);

    /**
     * 发送邮箱验证码
     *
     * @param email
     * @return
     */
    ApiResponse sendEmailCode(String email);

    /**
     * 绑定邮箱
     *
     * @param emailBindingReqMsg
     * @return
     */
    ApiResponse emailBiding(EmailBindingReqMsg emailBindingReqMsg);
}
