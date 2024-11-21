package com.cabin.ter.constants.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaoye
 * @date Created in 2024-05-23 11:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginSuccessResp {
    private String token;
    /**
     * token类型
     */
    private String tokenType = "Bearer";
}
