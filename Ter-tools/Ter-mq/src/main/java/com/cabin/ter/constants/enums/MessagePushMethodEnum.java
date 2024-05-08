package com.cabin.ter.constants.enums;

import com.cabin.ter.constants.enums.IStatus;
import lombok.Getter;

/**
 * <p>
 *     消息推送方式
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-01 12:41
 */
@Getter
public enum MessagePushMethodEnum implements IStatus {
    EMAIL_MESSAGE(6001,"邮箱消息通知"),
    USER_WEB_MESSAGE(6002,"用户主页通知"),
    WX_NO_MESSAGE(6003,"微信公众号通知"),
    SHORT_MESSAGE(6004,"短信通知"),
    ;
    private Integer code;
    private String message;

    MessagePushMethodEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
