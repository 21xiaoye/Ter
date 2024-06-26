package com.cabin.ter.constants.participant.msg;

import com.cabin.ter.constants.dto.MQBaseMessage;
import com.cabin.ter.constants.enums.MessagePushMethodEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

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
     * 推送方式 (邮箱广播推送，短信推送，微信公众号推送......)
     * @see MessagePushMethodEnum
     */
    private MessagePushMethodEnum pushMethod;
    /**
     * 接受者频道Id或邮箱地址
     */
    @Schema(name = "toAddress",description = "接收者地址")
    private String toAddress;
    /**
     * 消息主体
     */
    @Schema(name = "content",description = "消息主体")
    private String content;

    public WebSocketSingleParticipant(String toAddress, String content) {
        this.toAddress = toAddress;
        this.content = content;
    }
}
