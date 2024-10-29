package com.cabin.ter.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *  <p>
 *      用户信息返回
 *  </p>
 * @author xiaoye
 * @data Created in 2024-05-28 10:54
 */
@Data
@Schema(description = "用户详情")
public class UserInfoResp {
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

    @Schema(description = "用户openId", name = "openId")
    private String openId;

    @Schema(description = "用户角色 40001:管理员 40002:普通用户", name = "roleId")
    private Integer roleId;
}
