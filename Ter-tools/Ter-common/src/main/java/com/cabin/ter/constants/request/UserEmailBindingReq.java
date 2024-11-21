package com.cabin.ter.constants.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

/**
 * @author xiaoye
 * @date Created in 2024/11/5 10:31
 */
@Data
public class UserEmailBindingReq {
    private String openId;
    @Email
    private String email;
    private String code;
}
