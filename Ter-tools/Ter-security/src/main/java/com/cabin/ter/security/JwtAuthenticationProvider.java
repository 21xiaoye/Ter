package com.cabin.ter.security;

import com.cabin.ter.admin.domain.PermissionDomain;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.admin.mapper.PermissionDomainMapper;
import com.cabin.ter.vo.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


import java.util.List;


@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private PermissionDomainMapper permissionDomainMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UserDomain userDomain = (UserDomain)authentication.getPrincipal();
        Integer roleId = userDomain.getRoleId();

        // 根据角色Id查询用户权限
        List<Long> permissionIds = permissionDomainMapper.findPermissionIdsByRoleId(roleId);
        List<PermissionDomain> permissions = permissionDomainMapper.selectPermissionsByPermissionIds(permissionIds);
        UserPrincipal userPrincipal = UserPrincipal.create(userDomain, permissions);

        return new UsernamePasswordAuthenticationToken(userPrincipal , null, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
