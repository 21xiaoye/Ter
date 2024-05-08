package com.cabin.ter.service;

import com.cabin.ter.constants.vo.request.LoginAndRegisterRequest;
import com.cabin.ter.constants.vo.response.ApiResponse;

/**
 * @author xiaoye
 * @date Created in 2024-04-23 14:46
 */
public interface UserService {
    ApiResponse userLogin(LoginAndRegisterRequest loginRequest);
    ApiResponse userRegister(LoginAndRegisterRequest loginRequest);
}
