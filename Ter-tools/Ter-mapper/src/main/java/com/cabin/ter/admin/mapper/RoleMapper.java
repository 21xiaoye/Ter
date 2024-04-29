package com.cabin.ter.admin.mapper;


import com.cabin.ter.admin.domain.Role;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 *     角色mapper层
 * </p>
 * @author xiaoye
 * @date Created in 2024-04-27 16:18
 */

public interface RoleMapper {
    /**
     * 根据用户Id查询角色Id列表
     * @param userId    用户Id
     * @return  角色Id列表
     */
    @Select("SELECT roleId FROM ter_user_role WHERE userId = #{userId}")
    List<Long> findRoleIdsByUserId(Long userId);

    /**
     * 根据角色Id查询角色信息
     *
     * @param roleId    角色Id
     * @return  Role
     */
    @Select("SELECT roleId, roleName, description FROM ter_role WHERE roleId=#{roleId}")
    Optional<Role> findRoleByRoleId(Long roleId);

    /**
     * 根据角色Id列表查询角色信息列表
     *
     * @param roleIds
     * @return  角色信息列表
     */
    List<Role> findRolesByRoleIds(List<Long> roleIds);
}
