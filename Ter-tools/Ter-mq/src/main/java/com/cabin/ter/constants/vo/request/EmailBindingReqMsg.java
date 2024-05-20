package com.cabin.ter.constants.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *     邮箱绑定请求体
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-20 14:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "邮箱绑定参数")
public class EmailBindingReqMsg {
    @Schema(name = "openId", description = "用户微信openId")
    private String openId;

    @Schema(name = "userEmail",description = "用户邮箱")
    @NotBlank(message = "邮箱不能为空")
    @Email
    private String email;

    @Schema(name = "code",description = "邮箱验证码")
    private String code;
}
