package com.cabin.ter.constants.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendResp {
    public static final Integer ONLINE = 1;
    public static final Integer NO_ONLINE = 0;
    @Schema(description = "用户id",name = "userId")
    private Long userId;

    @Schema(description = "用户昵称",name = "userName")
    private String userName;

    @Schema(description = "用户头像", name = "userAvatar")
    private String userAvatar;

    @Schema(description = "用户邮箱", name = "userEmail")
    private String userEmail;

    @Schema(description = "性别 1为男性，2为女性",name = "sex")
    private Integer sex;

    @Schema(description = "在线状态 1在线 0离线",name = "lineStatus")
    private Integer lineStatus;
}
