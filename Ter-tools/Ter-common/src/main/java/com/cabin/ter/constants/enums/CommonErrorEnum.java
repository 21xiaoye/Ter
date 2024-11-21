package com.cabin.ter.constants.enums;

import com.cabin.ter.constants.enums.IStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiaoye
 * @date Created in 2024-05-29 14:52
 */
@AllArgsConstructor
@Getter
public enum CommonErrorEnum implements IStatus {

    SYSTEM_ERROR(-1, "系统出小差了，请稍后再试哦~~"),
    PARAM_VALID(-2, "参数校验失败{0}"),
    FREQUENCY_LIMIT(-3, "请求太频繁了，请稍后再试哦~~"),
    LOCK_LIMIT(-4, "请求太频繁了，请稍后再试哦~~"),
    ;
    private final Integer status;
    private final String message;

}
