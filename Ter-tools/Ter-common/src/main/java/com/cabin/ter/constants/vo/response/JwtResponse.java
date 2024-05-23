package com.cabin.ter.constants.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *     JWT 响应返回
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-04-19 16:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    /**
     * token 登录
     */
    private String token;
    /**
     * token类型
     */
    private String tokenType = "Bearer";

    public JwtResponse(String token){
        this.token = token;
    }
}
