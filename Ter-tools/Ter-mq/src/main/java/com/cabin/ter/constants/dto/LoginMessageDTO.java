package com.cabin.ter.constants.dto;

import lombok.Data;


/**
 * <p>
 *     将扫码登录返回信息推送给所有横向扩展的服务
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-11 11:11
 */
@Data
public class LoginMessageDTO extends MQBaseMessage  {
    private String openId;
    private String loginEmail;
    private Integer logonCode;
}
