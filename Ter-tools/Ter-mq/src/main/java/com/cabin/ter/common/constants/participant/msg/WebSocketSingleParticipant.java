package com.cabin.ter.common.constants.participant.msg;

import com.cabin.ter.common.constants.dto.MQBaseMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 *  <p>
 *      websocket 单点消息
 *  </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-02 21:44
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(name = "WebSocketSingleParticipant",description = "推送数据")
public class WebSocketSingleParticipant extends MQBaseMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 8445773977080406428L;
    /**
     * 消息主题
     */
    private String topic;
    /**
     * 消息标签
     */
    private int flag;
    /**
     * 通用值透传，可用于链路追踪
     */
    private Map<String, String> properties;
    /**
     * 接受者频道Id
     */
    @Schema(name = "channelId",description = "接收者频道Id")
    private String channelId;
    /**
     * 消息主体
     */
    @Schema(name = "content",description = "消息主体")
    private String content;

    public WebSocketSingleParticipant(String channelId, String content) {
        this.channelId = channelId;
        this.content = content;
    }
}
