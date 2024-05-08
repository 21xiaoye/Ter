package com.cabin.ter.constants.enums;

import com.cabin.ter.constants.vo.response.WsLoginSuccess;
import com.cabin.ter.constants.vo.response.WsLoginUrl;
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
    LOGIN_URL(1, "登录二维码返回", WsLoginUrl.class),
    LOGIN_SCAN_SUCCESS(2, "用户扫描成功等待授权", null),
    LOGIN_SUCCESS(3, "用户登录成功返回用户信息", WsLoginSuccess.class),
    ;

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