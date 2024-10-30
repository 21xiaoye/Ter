package com.cabin.ter.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * <p>
 *     申请好友请求参数
 * </p>
 * @author xiaoye
 * @date Created in 2024-05-27 10:20
 */
@Data
@Schema(name ="FriendApplyReq",description = "好友申请请求参数信息")
public class FriendApplyReq {

    @NotBlank
    @Schema(name = "applyMessage",description = "申请信息")
    private String applyMessage;

    @NotBlank
    @Schema(name="targetUid",description = "好友uId")
    private Long targetId;

    @NotBlank
    @Schema(name = "remark", description = "备注")
    private String remark;
}
