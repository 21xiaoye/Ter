package com.cabin.ter.common.constants.participant.msg;

import com.cabin.ter.common.constants.dto.MQBaseMessage;
import com.cabin.ter.common.constants.enums.MessagePushMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *     WebSocket 广播消息
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-05 12:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketWideParticipant extends MQBaseMessage implements MessageParticipant, Serializable {
    /**
     * 推送方式 (邮箱广播推送，短信推送，微信公众号推送......)
     * @see com.cabin.ter.common.constants.enums.MessagePushMethodEnum
     */
    private MessagePushMethodEnum pushMethod;
    /**
     * 消息格式
     * @see com.cabin.ter.common.constants.participant.constant.MessageFormatConstants
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
