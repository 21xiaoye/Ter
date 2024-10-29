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
@Schema(name = "ApprovalFriendReq",description = "审批-删除好友请求参数信息")
@Data
public class ApprovalFriendReq {
    @Schema(name = "applyId", description = "申请记录Id")
    private Long applyId;
    @Schema(name = "targetId", description = "申请者Id")
    private Long targetId;
    @Schema(name = "applyStatus", description = "审批状态 0拒绝好友申请 1待审批 2同意好友申请 3删除好友申请记录")
    private Integer applyStatus;
}