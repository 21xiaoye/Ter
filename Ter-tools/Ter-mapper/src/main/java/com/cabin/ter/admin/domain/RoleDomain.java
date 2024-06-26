package com.cabin.ter.admin.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *     角色
 * </p>
 * @author xiaoye
 * @date Created in 2024-04-27 15:46
 */
@Data
public class RoleDomain implements Serializable {
    /**
     * 主键
     */
    private Integer roleId;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;
}
