package com.cabin.ter.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * <p>
 *     申请好友请求参数
 * </p>
 * @author xiaoye
 * @date Created in 2024-05-27 10:20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "好友申请请求参数信息")
public class FriendApplyReq {

    @NotBlank
    @Schema(name = "msg",description = "申请信息")
    private String applyMessage;

    @NotNull
    @Schema(name="targetUid",description = "好友uid")
    private Long targetId;

}
