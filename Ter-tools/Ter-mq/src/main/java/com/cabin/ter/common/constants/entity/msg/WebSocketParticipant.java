package com.cabin.ter.common.constants.entity.msg;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.naming.ldap.PagedResultsControl;
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
@Schema(name = "WebSocketParticipant",description = "推送数据")
public class WebSocketParticipant implements MessageParticipant, Serializable {
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
}
