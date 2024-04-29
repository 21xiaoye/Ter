package com.cabin.ter.common.service.Impl;

import com.cabin.ter.admin.domain.User;
import com.cabin.ter.admin.mapper.UserMapper;
import com.cabin.ter.common.payload.LoginRequest;
import com.cabin.ter.common.service.UserService;
import com.cabin.ter.config.IdConfig;
import com.cabin.ter.config.SaltConfig;
import com.cabin.ter.constants.ApiResponse;
import com.cabin.ter.util.AsserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xiaoye
 * @date Created in 2024-04-23 14:49
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private IdConfig idConfig;
    @Autowired
    private SaltConfig saltConfig;
    @Autowired
    private UserMapper userMapper;


    @Override
    public ApiResponse userLogin(LoginRequest user) {
        AsserUtil.fastFailValidate(user);
        return ApiResponse.ofSuccess();
    }

    @Override
    public ApiResponse userRegister(User user) {

        return null;
    }
}
