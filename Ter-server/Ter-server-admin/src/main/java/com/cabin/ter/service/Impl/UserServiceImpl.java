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
import com.cabin.ter.constants.participant.constant.TopicConstant;
import com.cabin.ter.constants.dto.EmailMessageDTO;
import com.cabin.ter.template.RocketMQEnhanceTemplate;
import com.cabin.ter.adapter.MQMessageBuilderAdapter;
import com.cabin.ter.vo.enums.OperateEnum;
import com.cabin.ter.vo.request.LoginAndRegisterRequest;
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
    private Snowflake snowflake;
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
    private UserInfoCache userInfoCache;
    @Override
    public ApiResponse userLogin(LoginAndRegisterRequest request) {
        String userEmail = request.getUserEmail();
        UserDomain userDomain = userInfoCache.getUserInfoBatch(userEmail);
        AsserUtil.isEmpty(userDomain, Status.USER_NO_OCCUPY);
        if (OperateEnum.of(request.getType()).equals(OperateEnum.USER_LOGIN)) {
            String salt = userDomain.getSalt();
            String userPasswd = request.getUserPasswd();
            userPasswd = myPasswordEncoder.passwdEncryption(userPasswd, salt);
            if(!passwordEncoder.matches(userPasswd, userDomain.getUserPasswd())){
                throw new BaseException(Status.USERNAME_PASSWORD_ERROR);
            }
        }
        if (OperateEnum.of(request.getType()).equals(OperateEnum.USER_CODE)) {
            String code = redisCache.get(RedisKey.getKey(RedisKey.SAVE_EMAIL_CODE, request.getUserEmail()),String.class);
            log.info("[{}]验证码为=[{}]",request.getUserEmail(),code);
            AsserUtil.equal(request.getCode(), code, "验证码错误");
        }
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDomain, null, null));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String jwt = jwtUtil.createJWT(authenticate, request.getRememberMe());
        return ApiResponse.ofSuccess(new JwtResponse(jwt));
    }

    @Override
    public UserDomain userRegister(LoginAndRegisterRequest request) {
        UserDomain userDomain = userInfoCache.getUserInfoBatch(request.getUserEmail());
        AsserUtil.nonEmpty(userDomain, Status.USER_OCCUPY);
        String code = redisCache.get(RedisKey.getKey(RedisKey.SAVE_EMAIL_CODE, request.getUserEmail()), String.class);
        AsserUtil.equal(request.getCode(), code, "验证码错误");
        return createUser(request);
    }
    @Async
    public void sendMailCode(String userEmail,Integer operationType) {
        String code = VerifyUtil.generateCode(6);
//        Context context  = new Context();
//        context.setVariable("code", Arrays.asList(code.split("")));
//        // TODO: 文件太大，超过MQ 消息最大限制，需要进行压缩或者改变 MQ 消息最大限制，这里我暂时不做考虑，直接发送验证码
//        String emailCodeContext = templateEngine.process("EmailMediaCodec", context);
        redisCache.set(RedisKey.getKey(RedisKey.SAVE_EMAIL_CODE,userEmail),code,60, TimeUnit.SECONDS);

        EmailMessageDTO emailMessageParticipant = MQMessageBuilderAdapter.buildEmailMessageParticipant(OperateEnum.of(operationType).getMessage(), userEmail, code, SourceEnum.EMAIL_BINDING_SEND_CODE_SOURCE);
        rocketMQEnhanceTemplate.send(TopicConstant.ROCKET_SINGLE_PUSH_MESSAGE_TOPIC,  emailMessageParticipant);
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
    private UserDomain createUser(LoginAndRegisterRequest request){
        String salt = myPasswordEncoder.generateSalt();
        String userPasswd = request.getUserPasswd();
        String saltEncode = myPasswordEncoder.passwdEncryption(userPasswd, salt);
        String encodePasswd = MyPasswordEncoderFactory.getInstance().encode(EncryptionEnum.MD5, saltEncode);
        UserDomain userDomain = UserDomain.builder()
                .userId(snowflake.nextId())
                .userEmail(request.getUserEmail())
                .userPasswd(encodePasswd)
                .userName(request.getUserEmail())
                .salt(salt)
                .createTime(System.currentTimeMillis())
                .roleId(Objects.isNull(request.getRoleId()) ? RoleEnum.ORDINARY.getStatus() : RoleEnum.ADMIN.getStatus())
                .sex('1')
                .build();
        if (request.getRoleId() != null) {
            userDomain.setRoleId(request.getRoleId());
        }
        return userDomain;
    }
}
