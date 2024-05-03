package com.cabin.ter.common.constants.enums;


import com.cabin.ter.constants.enums.IStatus;
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
 * @date Created in 2024-05-03 15:35
 */

@Getter
public enum WSReqTypeEnum implements IStatus {
    WX_LOGIN(1, "请求微信登录二维码"),
    HEARTBEAT(2, "心跳包"),
    AUTHORIZE(3, "登录认证"),
    ;


    private Integer code;
    private String message;

    WSReqTypeEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    private static Map<Integer, WSReqTypeEnum> cache;

    static {
        cache = Arrays.stream(WSReqTypeEnum.values()).collect(Collectors.toMap(WSReqTypeEnum::getCode, Function.identity()));
    }

    public static WSReqTypeEnum of(Integer type) {
        return cache.get(type);
    }
}
