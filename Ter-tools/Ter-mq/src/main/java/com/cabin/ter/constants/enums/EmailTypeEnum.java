package com.cabin.ter.constants.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum EmailTypeEnum implements IStatus{
    SYSTEM_VERIFICATION_CODE(1001,"系统验证码邮件"),
    SYSTEM_WEL_COME(1002,"系统欢迎邮件");
    private Integer status;
    private String message;
    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
    EmailTypeEnum(Integer status, String message){
        this.status = status;
        this.message = message;
    }
}
