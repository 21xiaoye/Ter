package com.cabin.ter.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 *     审批好友申请参数
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-10-27 21:43
 */
@Schema(name = "审批好友请求参数信息")
@Data
public class ApprovalFriendReq {
    @Schema(name = "applyId", description = "申请记录Id")
    private Long applyId;
    @Schema(name = "applyStatus", description = "审批状态")
    private Integer applyStatus;
}
