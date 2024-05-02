package com.cabin.ter.common.constants.entity.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *  <p>
 *      websocket 推送消息
 *  </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-02 21:44
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WebSocketParticipant implements MessageParticipant, Serializable {
    /**
     * 接受者频道Id
     */
    private String channelId;
    /**
     * 消息主体
     */
    private String content;
}
