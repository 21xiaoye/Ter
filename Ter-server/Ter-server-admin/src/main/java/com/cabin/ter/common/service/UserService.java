package com.cabin.ter.common.service;

import com.cabin.ter.common.payload.LoginRequest;
import com.cabin.ter.constants.vo.response.ApiResponse;

/**
 * @author xiaoye
 * @date Created in 2024-04-23 14:46
 */
public interface UserService {
    ApiResponse userLogin(LoginRequest loginRequest);
    ApiResponse userRegister(LoginRequest loginRequest);
}
