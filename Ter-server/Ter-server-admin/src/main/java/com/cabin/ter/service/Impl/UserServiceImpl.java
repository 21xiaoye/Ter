package com.cabin.ter.service.Impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import com.cabin.ter.adapter.TxObjectStorageAdapter;
import com.cabin.ter.adapter.UserAdapter;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.admin.mapper.UserDomainMapper;

import com.cabin.ter.cache.RedisCache;
import com.cabin.ter.cache.UserInfoCache;
import com.cabin.ter.constants.RedisKey;
import com.cabin.ter.constants.domain.OssReq;
import com.cabin.ter.constants.enums.*;
import com.cabin.ter.constants.TopicConstant;
import com.cabin.ter.constants.dto.EmailMessageDTO;
import com.cabin.ter.listener.event.UserOnlineEvent;
import com.cabin.ter.template.RocketMQEnhanceTemplate;
import com.cabin.ter.adapter.MQMessageBuilderAdapter;
import com.cabin.ter.vo.enums.OperateEnum;
import com.cabin.ter.vo.request.LoginAndRegisterReq;
import com.cabin.ter.security.MyPasswordEncoder;
import com.cabin.ter.exception.BaseException;
import com.cabin.ter.factory.MyPasswordEncoderFactory;
import com.cabin.ter.service.UserService;
import com.cabin.ter.util.JwtUtil;
import com.cabin.ter.util.VerifyUtil;
import com.cabin.ter.constants.vo.response.JwtResponse;
import com.cabin.ter.constants.vo.response.ApiResponse;
import com.cabin.ter.util.AsserUtil;
import com.cabin.ter.vo.response.UserInfoResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaoye
 * @date Created in 2024-04-23 14:49
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDomainMapper userDomainMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MyPasswordEncoder myPasswordEncoder;
    @Autowired
    private RocketMQEnhanceTemplate rocketMQEnhanceTemplate;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private TxObjectStorageAdapter txObjectStorageAdapter;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private UserInfoCache userInfoCache;
    @Override
    public ApiResponse userLogin(LoginAndRegisterReq request) {
        String userEmail = request.getUserEmail();
        UserDomain userDomain = userInfoCache.getUserInfoBatch(userEmail);
        AsserUtil.isEmpty(userDomain, Status.USER_NO_OCCUPY);
        if (OperateEnum.of(request.getOperationType()).equals(OperateEnum.USER_LOGIN)) {
            String salt = userDomain.getSalt();
            String userPasswd = request.getUserPasswd();
            userPasswd = myPasswordEncoder.passwdEncryption(userPasswd, salt);
            if(!passwordEncoder.matches(userPasswd, userDomain.getUserPasswd())){
                throw new BaseException(Status.USERNAME_PASSWORD_ERROR);
            }
        }
        if (OperateEnum.of(request.getOperationType()).equals(OperateEnum.USER_CODE)) {
            String code = redisCache.get(RedisKey.getKey(RedisKey.SAVE_EMAIL_CODE, request.getUserEmail()),String.class);
            log.info("[{}]验证码为=[{}]",request.getUserEmail(),code);
            AsserUtil.equal(request.getCode(), code, "验证码错误");
        }
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDomain, null, null));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String jwt = jwtUtil.createJWT(authenticate, request.getRememberMe());
        // 推送上线通知
        applicationEventPublisher.publishEvent(new UserOnlineEvent(this, userDomain.getUserId(), System.currentTimeMillis()));
        return ApiResponse.ofSuccess(new JwtResponse(jwt));
    }

    @Override
    public UserDomain userRegister(LoginAndRegisterReq request) {
        UserDomain userDomain = userInfoCache.getUserInfoBatch(request.getUserEmail());
        AsserUtil.nonEmpty(userDomain, Status.USER_OCCUPY);
        String code = redisCache.get(RedisKey.getKey(RedisKey.SAVE_EMAIL_CODE, request.getUserEmail()), String.class);
        AsserUtil.equal(request.getCode(), code, "验证码错误");

        String salt = myPasswordEncoder.generateSalt();
        String userPasswd = request.getUserPasswd();
        String saltEncode = myPasswordEncoder.passwdEncryption(userPasswd, salt);
        String encodePasswd = MyPasswordEncoderFactory.getInstance().encode(EncryptionEnum.MD5, saltEncode);
        return UserAdapter.buildUserDomain(request, encodePasswd, salt);
    }
    @Override
    public void sendMailCode(String userEmail,Integer operationType) {
        String code = VerifyUtil.generateCode(6);
        redisCache.set(RedisKey.getKey(RedisKey.SAVE_EMAIL_CODE,userEmail),code,60, TimeUnit.SECONDS);
        sendMail(userEmail, code, OperateEnum.of(operationType),EmailTypeEnum.SYSTEM_VERIFICATION_CODE, SourceEnum.EMAIL_BINDING_SEND_CODE_SOURCE);
    }
    @Override
    public UserInfoResp getUserInfo(Long userId) {
        UserDomain userInfo =userInfoCache.getUserInfo(userId);
        return UserAdapter.buildUserInfoResp(userInfo);
    }
    @Transactional
    @Override
    public void saveUser(UserDomain userDomain){
        userDomainMapper.insertTerUser(userDomain);
        redisCache.mset(RedisKey.getKey(RedisKey.USER_ONLINE_INFO, userDomain.getUserId()), userDomain,5*60);
        sendMail(userDomain.getUserEmail(), userDomain.getUserName(), OperateEnum.WEL_COME, EmailTypeEnum.SYSTEM_WEL_COME, SourceEnum.TEST_SOURCE);
    }

    @Async
    public void sendMail(String email, String content, OperateEnum operateEnum,EmailTypeEnum emailType, SourceEnum source){
        EmailMessageDTO emailMessageParticipant = MQMessageBuilderAdapter.buildEmailMessageDTO(operateEnum.getMessage(), email, content, emailType,source);
        rocketMQEnhanceTemplate.send(TopicConstant.ROCKET_SINGLE_PUSH_MESSAGE_TOPIC,  emailMessageParticipant);
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
}
