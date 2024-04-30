package com.cabin.ter.common.service.Impl;

import com.cabin.ter.admin.mapper.UserMapper;
import com.cabin.ter.common.payload.LoginRequest;
import com.cabin.ter.common.service.UserService;
import com.cabin.ter.common.util.JwtUtil;
import com.cabin.ter.common.vo.JwtResponse;
import com.cabin.ter.config.IdConfig;
import com.cabin.ter.constants.ApiResponse;
import com.cabin.ter.util.AsserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public ApiResponse userLogin(LoginRequest loginRequest) {
        AsserUtil.fastFailValidate(loginRequest);
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserEmail(), loginRequest.getUserPasswd()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String jwt = jwtUtil.createJWT(authenticate, loginRequest.getRememberMe());
        return ApiResponse.ofSuccess(new JwtResponse(jwt));
    }

    @Override
    public ApiResponse userRegister(LoginRequest loginRequest) {
        String encode = passwordEncoder.encode(loginRequest.getUserPasswd());
        return null;
    }
}
