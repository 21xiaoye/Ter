package com.cabin.ter.common.constants.enums;

import com.cabin.ter.constants.enums.IStatus;
import lombok.Getter;

/**
 * <p>
 *     消息通知方式
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-01 12:41
 */
@Getter
public enum MessageEnum implements IStatus {
    EMAIL_MESSAGE(6001,"邮箱消息通知"),
    WEBSOCKET_MESSAGE(6002,"websocket通知"),
    WX_NO_MESSAGE(6003,"微信公众号通知"),
    SHORT_MESSAGE(6004,"短信通知"),
    ;
    private Integer code;
    private String message;

    MessageEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}