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
        checkUserExistence(loginRequest.getUserEmail());

        String salt = myPasswordEncoder.generateSalt();
        String encryptedPassword = encryptPassword(loginRequest.getUserPasswd(), salt);

        User user = buildUser(loginRequest, salt, encryptedPassword);
        validateAndSetRoles(user, loginRequest.getRoleId());

        userMapper.insertTerUser(user);
        roleMapper.insertUserRole(user);
        return ApiResponse.ofSuccess();
    }

    private void checkUserExistence(String userEmail) {
        userMapper.findByUserEmail(userEmail).ifPresent(u -> {
            throw new BaseException(Status.USER_OCCUPY);
        });
    }

    private String encryptPassword(String password, String salt) {
        String saltEncode = myPasswordEncoder.passwdEncryption(password, salt);
        return MyPasswordEncoderFactory.getInstance().encode(EncryptionEnum.MD5, saltEncode);
    }

    private User buildUser(LoginRequest request, String salt, String password) {
        return User.builder()
                .userId(snowflake.nextId())
                .userEmail(request.getUserEmail())
                .userPasswd(password)
                .userName(request.getUserEmail())
                .salt(salt)
                .createTime(System.currentTimeMillis())
                .build();
    }

    private void validateAndSetRoles(User user, Integer roleId) {
        RoleEnum role = RoleEnum.of(roleId);
        if (Objects.isNull(role)) {
            throw new BaseException(Status.PARAM_NOT_MATCH);
        }
        switch (role){
            case ADMIN :
                user.setRoleIdList(Arrays.asList(RoleEnum.ADMIN.getCode(),RoleEnum.ORDINARY.getCode()));
                break;
            case ORDINARY:
                user.setRoleIdList(Arrays.asList(RoleEnum.ORDINARY.getCode()));
                break;
            default:
                log.error("未知角色");
                throw new BaseException(Status.PARAM_NOT_MATCH);
        }
    }
}
