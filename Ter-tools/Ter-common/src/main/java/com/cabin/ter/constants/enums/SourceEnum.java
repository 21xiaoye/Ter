package com.cabin.ter.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 *      MQ 消息来源枚举
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-05 21:59
 */
@Getter
@AllArgsConstructor
public enum SourceEnum{
    CHAT_SOURCE_RETRY("SOURCE_RETRY"), // mq消息重试
    TEST_SOURCE("MESSAGE_TEST"), // mq测试
    USER_REGISTER("USER_REGISTER"), // 用户注册
    EMAIL_BINDING_SEND_CODE_SOURCE("email_binding_send_code"), // 邮箱绑定
    SYSTEM_WEL_COME_SOURCE("system_wel_come"), // 系统欢迎
    WX_SCAN_CODE_SOURCE("wx_scan_code_source"), // 用户扫码成功
    USER_ONLINE_SOURCE("user_online_source"), // 用户上线
    USER_OFFLINE_SOURCE("user_offline_source"), // 用户下线
    CHAT_MESSAGE_SOURCE("chat_message_source"); // 用户聊天消息
    private final String source;
}
