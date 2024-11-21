package com.cabin.ter.chat.domain.contact;

import com.cabin.ter.constants.enums.HotFlagEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Description: 房间详情
 * Author: <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-07-22
 */
@Data
public class RoomBaseInfo {
    @Schema(name = "roomId", description = "房间id")
    private Long roomId;
    @Schema(name = "name", description = "会话名称")
    private String name;
    @Schema(name = "avatar",description = "会话头像")
    private String avatar;
    @Schema(name = "type", description = "房间类型 1群聊 2单聊")
    private Integer type;
    /**
     * 是否全员展示 0否 1是
     *
     * @see HotFlagEnum
     */
    @Schema(name = "hotFlag", description = "是否全员展示")
    private Integer hotFlag;
    @Schema(name = "activeTime",description = "群最后消息的更新时间")
    private Long activeTime;
    @Schema(name = "lastMagId",description = "群最后一条消息的id")
    private Long lastMsgId;
}
