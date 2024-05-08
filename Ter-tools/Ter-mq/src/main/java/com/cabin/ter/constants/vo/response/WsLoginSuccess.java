package com.cabin.ter.constants.vo.response;


import lombok.Data;

/**
 * <p>
 *     扫码成功登录返回信息
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-03 20:56
 */
@Data
public class WsLoginSuccess {
    private Long uid;
    private String avatar;
    private String token;
    private String name;
}
