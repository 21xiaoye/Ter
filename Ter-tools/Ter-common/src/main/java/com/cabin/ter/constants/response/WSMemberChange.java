package com.cabin.ter.constants.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xiaoye
 * @date Created in 2024-05-31 20:18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WSMemberChange {
    public static final Integer CHANGE_TYPE_ADD = 1;
    public static final Integer CHANGE_TYPE_REMOVE = 2;
    @Schema(name = "roomId", description = "群组id")
    private Long roomId;
    @Schema(name = "uid", description = "变动uid集合")
    private Long uid;
    @Schema(name = "changeType",description = "变动类型 1加入群组 2移除群组")
    private Integer changeType;
    /**
     * @see com.cabin.ter.constants.enums.ChatActiveStatusEnum
     */
    @Schema(name = "activeStatus", description = "在线状态 1在线 2离线")
    private Integer activeStatus;
    @Schema(name = "lostOptTime", description = "最后一次上下线时间")
    private Date lastOptTime;
}
