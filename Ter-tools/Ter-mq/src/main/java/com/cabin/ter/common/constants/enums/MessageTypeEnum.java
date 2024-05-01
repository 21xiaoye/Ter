package com.cabin.ter.common.constants.enums;

import com.cabin.ter.constants.IStatus;
import lombok.Getter;

/**
 * <p>
 *    消息通知类型
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-01 13:14
 */

@Getter
public enum MessageTypeEnum implements IStatus {
    SYS_MESSAGE_TYPE(7001,"系统通知"),
    USER_MESSAGE_TYPE(7002,"用户通知");

    private Integer code;

    private String message;

    MessageTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
