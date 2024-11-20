package com.cabin.ter.constants.enums;

import com.cabin.ter.constants.vo.response.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *     前端请求类型枚举
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-03
 */
@AllArgsConstructor
@Getter
public enum WSRespTypeEnum {
    LOGIN_URL(1, "登录二维码返回", WsLoginUrlResp.class),
    LOGIN_SCAN_SUCCESS(2, "用户扫描成功等待授权", null),
    LOGIN_EMAIL_BINDING(3,"用户扫描成功，未绑定邮箱，进行邮箱绑定完成注册",null),
    LOGIN_SUCCESS(4, "用户登录成功返回用户信息", WSLoginSuccess.class),
    INVALIDATE_TOKEN(5, "使前端的token失效，意味着前端需要重新登录", null),
    MESSAGE(6, "新消息", null),
    USER_ONLINE(7, "用户上线", UserOnlineResp.class),
    USER_OFFLINE(8, "用户下线", UserOfflineResp.class),
    APPLY(10, "好友申请", WSApplyUserInfoResp.class),
    MEMBER_CHANGE(11, "成员变动", null);
    private final Integer type;
    private final String desc;
    private final Class dataClass;

    private static Map<Integer, WSRespTypeEnum> cache;

    static {
        cache = Arrays.stream(WSRespTypeEnum.values()).collect(Collectors.toMap(WSRespTypeEnum::getType, Function.identity()));
    }

    public static WSRespTypeEnum of(Integer type) {
        return cache.get(type);
    }
}