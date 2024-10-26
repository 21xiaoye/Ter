package com.cabin.ter.constants.participant.msg;

import com.cabin.ter.constants.dto.MQBaseMessage;
import com.cabin.ter.constants.enums.MessagePushMethodEnum;
import com.cabin.ter.constants.participant.constant.MessageFormatConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * <p>
 *     WebSocket 广播消息
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-05 12:26
 */
@Data
public class WebSocketWideParticipant extends MQBaseMessage implements Serializable {
    /**
     * 消息格式
     * @see MessageFormatConstants
     */
    private String messageFormat;
    /**
     * 文本消息主体
     */
    private String content;
    /**
     * 二进制消息(视频等需要进行转码的数据)
     */
    private byte[] body;
}
