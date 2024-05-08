package com.cabin.ter.security;

import com.cabin.ter.service.CustomUserDetailService;
import com.cabin.ter.vo.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;


@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private CustomUserDetailService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MyPasswordEncoder myPasswordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userEmail = String.valueOf(authentication.getPrincipal());
        String userPasswd = String.valueOf(authentication.getCredentials());

        UserPrincipal userDetails = (UserPrincipal)userDetailsService.loadUserByUsername(userEmail);
        String salt = userDetails.getSalt();
        System.out.println(userDetails);
        userPasswd = myPasswordEncoder.passwdEncryption(userPasswd, salt);

        if(passwordEncoder.matches(userPasswd,userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(userDetails, userPasswd, userDetails.getAuthorities());
        }

        throw new BadCredentialsException("Error!!");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
