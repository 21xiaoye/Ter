package com.cabin.ter.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(name = "FriendApplyResp",description = "好友申请详情")
public class FriendApplyResp {
    public static final Integer USER_APPLY  = 1;
    public static final Integer TARGET_APPLY = 2;
    @Schema(name = "applyId", description = "申请记录Id")
    private Long applyId;

    @Schema(name = "userId",description = "用户id")
    private Long userId;

    @Schema(name = "userName",description = "用户昵称")
    private String userName;

    @Schema(name = "userAvatar",description = "用户头像")
    private String userAvatar;

    @Schema(name = "userEmail",description = "用户邮箱")
    private String userEmail;

    @Schema(name = "sex",description = "性别 1为男性，2为女性")
    private Integer sex;

    @Schema(name = "applyMessage", description = "申请信息")
    private String applyMessage;

    @Schema(name = "applyStatus", description = "申请状态")
    private Integer applyStatus;
    @Schema(name = "applyType", description = "好友申请类型 1:其它用户发出的好友申请 2:用户发出的好友申请")
    private Integer applyType;
}
