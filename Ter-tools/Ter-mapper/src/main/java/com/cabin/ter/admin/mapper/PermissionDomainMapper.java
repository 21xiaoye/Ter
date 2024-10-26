package com.cabin.ter.admin.mapper;

import com.cabin.ter.admin.domain.PermissionDomain;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *     权限mapper层
 * </p>
 * @author xiaoye
 * @date Created in 2024-04-27 16:19
 */

public interface PermissionDomainMapper {
    /**
     * 根据角色Id列表查询权限Id列表
     * @param roleIds   角色Id列表
     * @return  权限Id列表
     */

    List<Long> selectPermissionIdsByRoleIds(List<Integer> roleIds);

    @Select("SELECT permissionId FROM ter_role_permission WHERE roleId = #{roleId}")
    List<Long> findPermissionIdsByRoleId(Integer roleId);

    /**
     * 根据权限Id列表查询权限列表
     * @param permissionIds   权限Id列表
     * @return  权限列表
     */
    List<PermissionDomain> selectPermissionsByPermissionIds(List<Long> permissionIds);
}
