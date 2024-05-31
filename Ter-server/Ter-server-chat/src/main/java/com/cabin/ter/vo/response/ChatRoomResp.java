package com.cabin.ter.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xiaoye
 * @date Created in 2024-05-30 15:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomResp {
    @Schema(name = "roomId",description = "房间id")
    private Long roomId;
    @Schema(name = "type", description = "房间类型 1群聊 2单聊")
    private Integer type;
    @Schema(name = "hotFlag",description = "是否全员展示的会话 0否 1是")
    private Integer hotFlag;
    @Schema(name = "text",description = "最新消息")
    private String text;
    @Schema(name = "name", description = "会话名称")
    private String name;
    @Schema(name = "avatar",description = "会话头像")
    private String avatar;
    @Schema(name = "activeTime",description = "房间最后活跃时间(用来排序)")
    private Date activeTime;
    @Schema(name = "unreadCount",description = "未读数")
    private Integer unreadCount;
}

