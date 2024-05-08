package com.cabin.ter.constants.enums;

import com.cabin.ter.constants.enums.IStatus;
import io.swagger.v3.oas.annotations.media.Schema;
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
    TEST_SOURCE("MESSAGE_TEST","消息测试");

    private final String source;
    private final String description;
}
