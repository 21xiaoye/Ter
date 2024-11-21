package com.cabin.ter.constants.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * @author xiaoye
 * @date Created in 2024-05-29 11:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageReq implements Serializable {
    @Schema(name = "roomId", description = "房间id")
    private Long roomId;
    /**
     * @see com.cabin.ter.constants.enums.MessageTypeEnum
     */
    @Schema(name = "messageType", description = "消息类型")
    private Integer messageType;

    @Schema(name = "body", description = "消息内容")
    private Object body;

}
