package com.cabin.ter.constants.vo.request;

import com.cabin.ter.annotation.PasswordMatches;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * <p>
 *     登录-注册请求参数
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-04-28 11:11
 */
@Schema(name = "登录-注册请求参数")
@Data
public class LoginAndRegisterRequest {
    @Schema(name = "userEmail",description = "用户邮箱")
    @NotBlank(message = "邮箱不能为空")
    @Email
    private String userEmail;


    @Schema(name = "userPasswd",description = "用户密码")
    @PasswordMatches
    private String userPasswd;

    @Schema(name = "code",description = "用户验证码")
    @Min(value = 0,message = "验证码错误")
    private Integer code;

    @Schema(name = "roleId",description = "创建角色id 40001-管理员 40002-普通用户,默认创建普通用户")
    private Integer roleId;

    @Schema(name = "rememberMe",description = "是否记住我")
    private Boolean rememberMe = false;
}
