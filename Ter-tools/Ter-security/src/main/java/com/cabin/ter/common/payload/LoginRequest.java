package com.cabin.ter.common.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * <p>
 *     登录请求参数
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-04-28 11:11
 */
@Data
public class LoginRequest {
    @NotBlank(message = "邮箱不能为空")
    private String userEmail;
    @NotBlank(message = "密码不能为空")
    private String userPasswd;
    private Boolean rememberMe = false;
}
