package com.cabin.ter.chat.domain.contact;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

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
    /**
     * 房间类型 1群聊 2单聊
     */
    private Integer type;

    /**
     * 是否全员展示 0否 1是
     *
     * @see com.cabin.ter.chat.enums.HotFlagEnum
     */
    private Integer hotFlag;

    /**
     * 群最后消息的更新时间
     */
    @TableField("active_time")
    private Date activeTime;

    /**
     * 最后一条消息id
     */
    @TableField("last_msg_id")
    private Long lastMsgId;
}
