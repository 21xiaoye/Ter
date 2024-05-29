package com.cabin.ter.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author xiaoye
 * @date Created in 2024-05-29 11:03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResp {
    @Schema(name = "fromUser", description = "发送者信息")
    private UserInfo fromUser;

    @Schema(name = "message", description = "消息详情")
    private Message message;
    @Data
    public static class UserInfo{
        @Schema(name = "uid",description = "用户uid")
        private Long uid;
    }

    @Data
    public static class Message{
        @Schema(name = "messageId",description = "消息id")
        private Long messageId;

        @Schema(name = "roomId", description = "房间id")
        private Long roomId;

        @Schema(name = "sendTime", description = "消息发送时间")
        private Long sendTime;

        @Schema(name = "messageType", description = "消息类型")
        /**
         * @see com.cabin.ter.constants.enums.MessageTypeEnum
         */
        private Integer messageType;

        @Schema(name = "body", description = "消息内容")
        private Objects body;
    }
}
