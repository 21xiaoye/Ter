package com.cabin.ter.service;

import com.cabin.ter.admin.domain.PermissionDomain;
import com.cabin.ter.admin.domain.RoleDomain;
import com.cabin.ter.admin.domain.UserDomain;

import com.cabin.ter.admin.mapper.PermissionDomainMapper;
import com.cabin.ter.admin.mapper.RoleDomainMapper;
import com.cabin.ter.admin.mapper.UserDomainMapper;
import com.cabin.ter.vo.UserPrincipal;
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
    private UserDomainMapper userMapper;
    @Autowired
    private RoleDomainMapper roleMapper;
    @Autowired
    private PermissionDomainMapper permissionMapper;
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        UserDomain user = userMapper.findByUsernameOrEmailOrPhone(userEmail).orElseThrow(() -> new UsernameNotFoundException("未找到用户信息:" + userEmail));

        // 查询用户角色Id
        List<Integer> roleIdsByUserId = roleMapper.findRoleIdsByUserId(user.getUserId());
        List<RoleDomain> roles = roleMapper.findRolesByRoleIds(roleIdsByUserId);

        // 根据角色Id查询用户权限
        List<Integer> rolesIds = roles.stream().map(RoleDomain::getRoleId).collect(Collectors.toList());
        List<Long> permissionIds = permissionMapper.selectPermissionIdsByRoleIds(rolesIds);

        List<PermissionDomain> permissions = permissionMapper.selectPermissionsByPermissionIds(permissionIds);
        return UserPrincipal.create(user,roles,permissions);
    }
}
