package com.cabin.ter.vo.enums;

import com.cabin.ter.constants.enums.IStatus;
import com.cabin.ter.constants.enums.MessageTypeEnum;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum OperateEnum implements IStatus {
    USER_REGISTER(1001,"用户注册"),
    USER_LOGIN(1002,"用户登录"),
    USER_CODE(1003,"验证码登录"),
    WEL_COME(1004, "欢迎新用户"),
    USER_BINDING_EMAIL(1005,"绑定邮箱");
    private Integer status;
    private String message;
    OperateEnum(Integer status, String message){
        this.status = status;
        this.message = message;
    }
    private static Map<Integer, OperateEnum> cache;

    static {
        cache = Arrays.stream(OperateEnum.values()).collect(Collectors.toMap(OperateEnum::getStatus, Function.identity()));
    }
    public static OperateEnum of(Integer type) {
        return cache.get(type);
    }
    @Override
    public Integer getStatus() {
        return status;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
