package com.cabin.ter.vo.response;

import com.cabin.ter.admin.domain.UserDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Schema(description = "用户id",name = "uid")
    private Long uId;
    @Schema(description = "用户昵称",name = "name")
    private String userName;

    @Schema(description = "用户头像", name = "avatar")
    private String userAvatar;

    @Schema(description = "用户邮箱", name = "email")
    private String userEmail;

    @Schema(description = "性别 1为男性，2为女性",name = "sex")
    private Integer sex;

    @Schema(description = "用户 openId", name = "openId")
    private String openId;
}
