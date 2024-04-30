package com.cabin.ter.common.service.Impl;

import cn.hutool.core.lang.Snowflake;
import com.cabin.ter.admin.domain.User;
import com.cabin.ter.admin.mapper.RoleMapper;
import com.cabin.ter.admin.mapper.UserMapper;
import com.cabin.ter.common.exception.SecurityException;
import com.cabin.ter.common.payload.LoginRequest;
import com.cabin.ter.common.security.MyPasswordEncoder;
import com.cabin.ter.constants.IStatus;
import com.cabin.ter.constants.Status;
import com.cabin.ter.constants.enums.RoleEnum;
import com.cabin.ter.exception.BaseException;
import com.cabin.ter.factory.MyPasswordEncoderFactory;
import com.cabin.ter.common.service.UserService;
import com.cabin.ter.common.util.JwtUtil;
import com.cabin.ter.common.vo.JwtResponse;
import com.cabin.ter.config.IdConfig;
import com.cabin.ter.constants.ApiResponse;
import com.cabin.ter.constants.enums.EncryptionEnum;
import com.cabin.ter.util.AsserUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @author xiaoye
 * @date Created in 2024-04-23 14:49
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private Snowflake snowflake;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MyPasswordEncoder myPasswordEncoder;
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
        AsserUtil.fastFailValidate(loginRequest);
        Optional<User> byUsernameOrEmailOrPhone = userMapper.findByUsernameOrEmailOrPhone(loginRequest.getUserEmail());
        byUsernameOrEmailOrPhone.ifPresent(user -> {
            throw new BaseException(Status.USER_OCCUPY);
        });

        // 第一次 加盐 哈希加密
        String salt = myPasswordEncoder.generateSalt();
        String saltEncode = myPasswordEncoder.passwdEncryption(loginRequest.getUserPasswd(), salt);

        // 第二次加密
        String encode = MyPasswordEncoderFactory.getInstance().encode(EncryptionEnum.MD5,saltEncode);
        User userBuilder = User.builder().userId(snowflake.nextId())
                .userEmail(loginRequest.getUserEmail())
                .userPasswd(encode)
                .userName(loginRequest.getUserEmail())
                .salt(salt)
                .createTime(System.currentTimeMillis())
                .build();

        RoleEnum status = RoleEnum.of(loginRequest.getRoleId());
        if(Objects.isNull(status)){
            throw new BaseException(Status.PARAM_NOT_MATCH);
        }

        switch (status){
            case ADMIN :
                userBuilder.setRoleIdList(Arrays.asList(RoleEnum.ADMIN.getCode(),RoleEnum.ORDINARY.getCode()));
                break;
            case ORDINARY:
                userBuilder.setRoleIdList(Arrays.asList(RoleEnum.ORDINARY.getCode()));
                break;
            default:
                log.error("未知角色");
                throw new BaseException(Status.PARAM_NOT_MATCH);
        }
        roleMapper.insertUserRole(userBuilder);
        userMapper.insertTerUser(userBuilder);
        return ApiResponse.ofSuccess();
    }
}
