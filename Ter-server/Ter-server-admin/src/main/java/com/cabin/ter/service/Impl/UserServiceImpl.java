package com.cabin.ter.service.Impl;

import cn.hutool.core.lang.Snowflake;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.admin.mapper.RoleDomainMapper;
import com.cabin.ter.admin.mapper.UserDomainMapper;
import com.cabin.ter.constants.enums.MessagePushMethodEnum;
import com.cabin.ter.constants.enums.SourceEnum;
import com.cabin.ter.constants.participant.constant.TopicConstant;
import com.cabin.ter.constants.participant.msg.WebSocketSingleParticipant;
import com.cabin.ter.template.RocketMQEnhanceTemplate;
import com.cabin.ter.constants.vo.request.LoginAndRegisterRequest;
import com.cabin.ter.security.MyPasswordEncoder;
import com.cabin.ter.constants.enums.Status;
import com.cabin.ter.constants.enums.RoleEnum;
import com.cabin.ter.exception.BaseException;
import com.cabin.ter.factory.MyPasswordEncoderFactory;
import com.cabin.ter.service.UserService;
import com.cabin.ter.util.JwtUtil;
import com.cabin.ter.vo.JwtResponse;
import com.cabin.ter.constants.vo.response.ApiResponse;
import com.cabin.ter.constants.enums.EncryptionEnum;
import com.cabin.ter.util.AsserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

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
    private UserDomainMapper userMapper;
    @Autowired
    private RoleDomainMapper roleMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MyPasswordEncoder myPasswordEncoder;
    @Autowired
    private RocketMQEnhanceTemplate rocketMQEnhanceTemplate;
    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public ApiResponse userLogin(LoginAndRegisterRequest loginRequest) {
        AsserUtil.fastFailValidate(loginRequest);
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserEmail(), loginRequest.getUserPasswd()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String jwt = jwtUtil.createJWT(authenticate, loginRequest.getRememberMe());
        return ApiResponse.ofSuccess(new JwtResponse(jwt));
    }


    @Override
    public ApiResponse userRegister(LoginAndRegisterRequest loginRequest) {
        AsserUtil.fastFailValidate(loginRequest);
        checkUserExistence(loginRequest.getUserEmail());

        String salt = myPasswordEncoder.generateSalt();
        String encryptedPassword = encryptPassword(loginRequest.getUserPasswd(), salt);

        UserDomain user = buildUser(loginRequest, salt, encryptedPassword);
        validateAndSetRoles(user, loginRequest.getRoleId());

        userMapper.insertTerUser(user);
        roleMapper.insertUserRole(user);

        WebSocketSingleParticipant webSocketSingleParticipant = new WebSocketSingleParticipant();
        webSocketSingleParticipant.setKey(UUID.randomUUID().toString());
        webSocketSingleParticipant.setSource(SourceEnum.TEST_SOURCE.getSource());
        webSocketSingleParticipant.setContent("欢迎来到Ter");
        webSocketSingleParticipant.setSendTime(LocalDateTime.now());
        webSocketSingleParticipant.setPushMethod(MessagePushMethodEnum.EMAIL_MESSAGE);
        webSocketSingleParticipant.setToAddress(loginRequest.getUserEmail());
        rocketMQEnhanceTemplate.send(TopicConstant.ROCKET_SINGLE_PUSH_MESSAGE_TOPIC, TopicConstant.SOURCE_SINGLE_PUSH_TAG,webSocketSingleParticipant);
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

    private UserDomain buildUser(LoginAndRegisterRequest request, String salt, String password) {
        return UserDomain.builder()
                .userId(snowflake.nextId())
                .userEmail(request.getUserEmail())
                .userPasswd(password)
                .userName(request.getUserEmail())
                .salt(salt)
                .createTime(System.currentTimeMillis())
                .build();
    }

    private void validateAndSetRoles(UserDomain user, Integer roleId) {
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
