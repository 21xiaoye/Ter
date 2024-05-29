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
public enum SourceEnum    {
    CHAT_SOURCE_RETRY("SOURCE_RETRY","重试消息"),
    TEST_SOURCE("MESSAGE_TEST","消息测试"),
    EMAIL_BINDING_SEND_CODE_SOURCE("email_binding_send_code","邮箱绑定验证码发送"),
    MSG_SEND_MESSAGE_SOURCE(TopicConstant.SEND_MSG_TOPIC, "用户聊天消息");


    private final String source;
    private final String description;
}
