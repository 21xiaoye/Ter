package com.cabin.ter.sys.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *     系统服务属性
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-03 08:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysDomain {
    /**
     * 服务Id
     */
    private Long sysId;
    /**
     * 服务Ip
     */
    private String sysHost;
    /**
     * 服务端口
     */
    private Integer sysPort;
    /**
     * 服务类型
     */
    private String sysType;
}
