package com.cabin.ter.service.Impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import com.cabin.ter.adapter.TxObjectStorageAdapter;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.admin.mapper.RoleDomainMapper;
import com.cabin.ter.admin.mapper.UserDomainMapper;

import com.cabin.ter.cache.RedisCache;
import com.cabin.ter.constants.RedisKey;
import com.cabin.ter.constants.domain.OssReq;
import com.cabin.ter.constants.dto.LoginMessageDTO;
import com.cabin.ter.constants.enums.MessagePushMethodEnum;
import com.cabin.ter.constants.enums.SourceEnum;
import com.cabin.ter.constants.participant.constant.TopicConstant;
import com.cabin.ter.constants.participant.msg.WebSocketSingleParticipant;
import com.cabin.ter.constants.vo.request.EmailBindingReqMsg;
import com.cabin.ter.template.RocketMQEnhanceTemplate;
import com.cabin.ter.constants.vo.request.LoginAndRegisterRequest;
import com.cabin.ter.security.MyPasswordEncoder;
import com.cabin.ter.constants.enums.Status;
import com.cabin.ter.constants.enums.RoleEnum;
import com.cabin.ter.exception.BaseException;
import com.cabin.ter.factory.MyPasswordEncoderFactory;
import com.cabin.ter.service.UserService;
import com.cabin.ter.util.JwtUtil;
import com.cabin.ter.util.VerifyUtil;
import com.cabin.ter.constants.vo.response.JwtResponse;
import com.cabin.ter.constants.vo.response.ApiResponse;
import com.cabin.ter.constants.enums.EncryptionEnum;
import com.cabin.ter.util.AsserUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private TxObjectStorageAdapter txObjectStorageAdapter;

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

        UserDomain userDomain = userMapper.findByUserEmail(loginRequest.getUserEmail());

        if(Objects.nonNull(userDomain)){
            throw new BaseException(Status.USER_OCCUPY);
        }

        UserDomain user = this.createUserWithRoles(loginRequest);
        userMapper.insertTerUser(user);
        roleMapper.insertUserRole(user);

        this.buildEmailMessage(loginRequest.getUserEmail(), "欢迎来到 Ter",SourceEnum.TEST_SOURCE.getSource());

        return ApiResponse.ofSuccess();
    }

    @Override
    public ApiResponse sendEmailCode(EmailBindingReqMsg emailBindingReqMsg) {
        String code = VerifyUtil.generateCode(8);
        this.sendMailAsync(emailBindingReqMsg.getEmail(), emailBindingReqMsg.getOpenId(), code);
        return ApiResponse.ofSuccess("验证码已发送");
    }
    // TODO: 这里还没有写完整，应该返回用户token，用户权限范围
    @Override
    public ApiResponse emailBiding(EmailBindingReqMsg emailBindingReqMsg) {
        AsserUtil.fastFailValidate(emailBindingReqMsg);

        String code = redisCache.get(RedisKey.getKey(RedisKey.SAVE_EMAIL_CODE, emailBindingReqMsg.getOpenId()),String.class);
        log.info("[{}]验证码为=[{}]",emailBindingReqMsg.getEmail(),code);
        if(emailBindingReqMsg.getCode().equals(code)){
            UserDomain userDomain = userMapper.findByUserEmail(emailBindingReqMsg.getEmail());
            WxOAuth2UserInfo userInfo = redisCache.get(RedisKey.getKey(RedisKey.AUTHORIZE_WX, emailBindingReqMsg.getOpenId()), WxOAuth2UserInfo.class);
            // 微信用户再此之前未进行邮箱注册
            if(!Objects.nonNull(userDomain)){
                UserDomain user = UserDomain.builder()
                        .openId(emailBindingReqMsg.getOpenId())
                        .userEmail(emailBindingReqMsg.getEmail())
                        .userId(snowflake.nextId())
                        .userName(userInfo.getNickname())
                        .userAvatar(userInfo.getHeadImgUrl())
                        .build();
                this.validateAndSetRoles(user, null);
                userMapper.insertTerUser(user);
                roleMapper.insertUserRole(user);
            }else{
                userMapper.updateUserOpenId(emailBindingReqMsg.getEmail(), emailBindingReqMsg.getOpenId());
            }
            Integer loginCode = redisCache.get(RedisKey.getKey(RedisKey.OPEN_ID_STRING, emailBindingReqMsg.getOpenId()), Integer.class);
            rocketMQEnhanceTemplate.send(TopicConstant.LOGIN_MSG_TOPIC, new LoginMessageDTO(emailBindingReqMsg.getOpenId(), emailBindingReqMsg.getEmail(), loginCode));
            return ApiResponse.ofSuccess();
        }
        return ApiResponse.ofSuccess("验证码错误");
    }
    @Async
    public void sendMailAsync(String email, String openId,String code) {
        redisCache.set(RedisKey.getKey(RedisKey.SAVE_EMAIL_CODE,openId),code,60, TimeUnit.SECONDS);
        // 将验证码放到thymeleaf页面中
        Context context  = new Context();
        context.setVariable("code", Arrays.asList(code.split("")));
        // TODO: 文件太大，超过MQ 消息最大限制，需要进行压缩或者改变 MQ 消息最大限制，这里我暂时不做考虑，直接发送验证码
        String emailCodeContext = templateEngine.process("EmailMediaCodec", context);
        this.buildEmailMessage(email, code, SourceEnum.EMAIL_BINDING_SEND_CODE_SOURCE.getSource());
    }

    public ApiResponse uploadAvatar(OssReq ossReq){
        String absolutePath = ossReq.isAutoPath() ? this.generateAutoPath(ossReq) : ossReq.getFilePath() + StrUtil.SLASH + ossReq.getFileName();
        return ApiResponse.ofSuccess(txObjectStorageAdapter.generateUploadUrl(absolutePath));
    }
    /**
     * 生成随机文件名，防止重复
     *
     * @return
     */
    private String generateAutoPath(OssReq req) {
        String uid = Optional.ofNullable(req.getUid()).map(String::valueOf).orElse("000000");
        cn.hutool.core.lang.UUID uuid = cn.hutool.core.lang.UUID.fastUUID();
        String suffix = FileNameUtil.getSuffix(req.getFileName());
        String yearAndMonth = DateUtil.format(new Date(), DatePattern.NORM_MONTH_PATTERN);
        return req.getFilePath() + StrUtil.SLASH + yearAndMonth + StrUtil.SLASH + uid + StrUtil.SLASH + uuid + StrUtil.DOT + suffix;
    }

    // TODO: 这里我觉得可以使用责任链模式来进行优化，检查用户是否存在-> 加盐哈希加密 -> 权限分配 -> 存储用户信息
    private UserDomain createUserWithRoles(LoginAndRegisterRequest loginRequest) {
        String salt = myPasswordEncoder.generateSalt();
        String encryptedPassword = this.encryptPassword(loginRequest.getUserPasswd(), salt);
        UserDomain user = this.buildUser(loginRequest, salt, encryptedPassword);
        this.validateAndSetRoles(user, loginRequest.getRoleId());
        return user;
    }

    /**
     * 构建邮箱消息
     *
     * @param email
     * @param content
     */

    private void buildEmailMessage(String email, String content, String source) {
        WebSocketSingleParticipant webSocketSingleParticipant = new WebSocketSingleParticipant();
        webSocketSingleParticipant.setKey(UUID.randomUUID().toString());
        webSocketSingleParticipant.setSource(source);
        webSocketSingleParticipant.setContent(content);
        webSocketSingleParticipant.setSendTime(LocalDateTime.now());
        webSocketSingleParticipant.setPushMethod(MessagePushMethodEnum.EMAIL_MESSAGE);
        webSocketSingleParticipant.setToAddress(email);

        CompletableFuture.runAsync(() ->
                rocketMQEnhanceTemplate.send(TopicConstant.ROCKET_SINGLE_PUSH_MESSAGE_TOPIC, TopicConstant.SOURCE_SINGLE_PUSH_TAG, webSocketSingleParticipant)
        );
    }

    /**
     * 密码加盐加密
     *
     * @param password
     * @param salt
     * @return
     */
    private String encryptPassword(String password, String salt) {
        String saltEncode = myPasswordEncoder.passwdEncryption(password, salt);
        return MyPasswordEncoderFactory.getInstance().encode(EncryptionEnum.MD5, saltEncode);
    }

    /**
     * 构建用户
     *
     * @param request
     * @param salt
     * @param password
     * @return
     */
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

    /**
     * 角色权限分配
     *
     * @param user
     * @param roleId
     */
    private void validateAndSetRoles(UserDomain user, Integer roleId) {
        // 默认用户角色为普通用户
        if(roleId == null){
            roleId = RoleEnum.ORDINARY.getStatus();
        }
        RoleEnum role = RoleEnum.of(roleId);
        if (Objects.isNull(role)) {
            throw new BaseException(Status.PARAM_NOT_MATCH);
        }
        switch (role){
            case ADMIN :
                user.setRoleIdList(Arrays.asList(RoleEnum.ADMIN.getStatus(),RoleEnum.ORDINARY.getStatus()));
                break;
            case ORDINARY:
                user.setRoleIdList(Arrays.asList(RoleEnum.ORDINARY.getStatus()));
                break;
            default:
                log.error("未知角色");
                throw new BaseException(Status.PARAM_NOT_MATCH);
        }
    }
}
