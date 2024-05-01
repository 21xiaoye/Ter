package com.cabin.ter.sys.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *     发送邮箱属性
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-01 14:50
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MailProperties {
    /**
     * 邮箱id
     */
    private Long mailOriginId;
    /**
     * 邮箱名称
     */
    private String mailName;
    /**
     * 邮箱密码
     */
    private String mailPasswd;
    /**
     * 服务商
     */
    private String mailHost;
    /**
     * 端口
     */
    private Integer port;
    /**
     * 协议
     */
    private String protocol;
    /**
     * 编码格式
     */
    private String encoding;
}
