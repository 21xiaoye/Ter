package com.cabin.ter.constants.enums;

import com.cabin.ter.constants.RedisKey;
import com.cabin.ter.constants.participant.constant.TopicConstant;
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
    CHAT_SOURCE_RETRY("SOURCE_RETRY"),
    TEST_SOURCE("MESSAGE_TEST"),
    USER_REGISTER("USER_REGISTER"),
    EMAIL_BINDING_SEND_CODE_SOURCE("email_binding_send_code"),
    SYSTEM_CLIENT_CONNECTION_SOURCE("system_client_connection"),
    SYSTEM_WEL_COME_SOURCE("system_wel_come"),
    WX_SCAN_CODE_SOURCE("wx_scan_code_source");
    private final String source;
}
