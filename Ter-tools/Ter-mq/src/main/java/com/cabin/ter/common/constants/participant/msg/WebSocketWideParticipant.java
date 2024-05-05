package com.cabin.ter.common.constants.participant.msg;

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
public class WebSocketWideParticipant implements MessageParticipant, Serializable {
    /**
     * 消息主体
     */
    private String content;
    /**
     * 推送时间
     */
    private LocalDateTime sendTime = LocalDateTime.now();
}
