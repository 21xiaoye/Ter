package com.cabin.ter.common.service;

import com.cabin.ter.admin.domain.Permission;
import com.cabin.ter.admin.domain.Role;
import com.cabin.ter.admin.domain.User;

import com.cabin.ter.admin.mapper.PermissionMapper;
import com.cabin.ter.admin.mapper.RoleMapper;
import com.cabin.ter.admin.mapper.UserMapper;
import com.cabin.ter.common.vo.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *     自定义用户查询
 * </p>
 * @author xiaoye
 * @dara Created in 2024-04-27 17:03
 */

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userMapper.findByUsernameOrEmailOrPhone(userEmail).orElseThrow(() -> new UsernameNotFoundException("未找到用户信息:" + userEmail));

        // 查询用户角色Id
        List<Long> roleIdsByUserId = roleMapper.findRoleIdsByUserId(user.getUserId());
        List<Role> roles = roleMapper.findRolesByRoleIds(roleIdsByUserId);


        // 根据角色Id查询用户权限
        List<Long> rolesIds = roles.stream().map(Role::getRoleId).collect(Collectors.toList());
        List<Long> permissionIds = permissionMapper.selectPermissionIdsByRoleIds(rolesIds);

        List<Permission> permissions = permissionMapper.selectPermissionsByPermissionIds(permissionIds);
        return UserPrincipal.create(user,roles,permissions);
    }
}
