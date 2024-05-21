package com.cabin.ter.constants.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *     角色枚举
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-04-30 19:14
 */

@Getter
public enum RoleEnum implements IStatus {
    ADMIN(40001,"管理员"),
    ORDINARY(40002,"普通用户");
    private Integer status;
    private String message;


    RoleEnum(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    private static Map<Integer, RoleEnum> cache;

    static {
        cache = Arrays.stream(RoleEnum.values()).collect(Collectors.toMap(RoleEnum::getStatus, Function.identity()));
    }

    public static RoleEnum of(Integer type) {
        return cache.get(type);
    }
}
