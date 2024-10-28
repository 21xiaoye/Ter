package com.cabin.ter.admin.domain;

import lombok.Builder;
import lombok.Data;

/**
 * 好友申请
 * @author xiaoye
 * @date Created in 2024/10/27 20:55
 */
@Data
@Builder
public class FriendApplyDomain {
    /**
     * 申请记录id
     */
    private Long applyId;
    /**
     * 申请者Id
     */
    private Long userId;
    /**
     * 被申请者Id
     */
    private Long targetId;
    /**
     * 申请状态 0拒绝 1待审批 2 同意 3删除申请记录
     */
    private Integer applyStatus;
    /**
     * 申请信息
     */
    private String applyMessage;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 审批时间
     */
    private Long approvalTime;
    /**
     * 删除时间
     */
    private Long deleteTime;
}
