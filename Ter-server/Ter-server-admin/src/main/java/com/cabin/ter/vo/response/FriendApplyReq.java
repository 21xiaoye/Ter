package com.cabin.ter.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * @author xiaoye
 * @date Created in 2024-05-27 10:20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "好友申请信息")
public class FriendApplyReq {

    @NotBlank
    @Schema(name = "msg",description = "申请信息")
    private String msg;

    @NotNull
    @Schema(name="targetUid",description = "好友uid")
    private Long targetUid;

}
