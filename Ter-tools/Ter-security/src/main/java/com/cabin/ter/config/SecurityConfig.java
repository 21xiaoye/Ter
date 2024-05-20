package com.cabin.ter.config;


import com.cabin.ter.handler.MyAccessDeniedHandler;
import com.cabin.ter.security.JwtAuthenticationFilter;
import com.cabin.ter.security.JwtAuthenticationProvider;
import com.cabin.ter.factory.MyPasswordEncoderFactory;
import com.cabin.ter.handler.MyUnauthorizedHandler;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * <p>
 *     Security配置
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-04-19 17:50
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private MyUnauthorizedHandler unauthorizedHandler;

    @Autowired
    private MyAccessDeniedHandler accessDeniedHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(){
        return new JwtAuthenticationProvider();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return MyPasswordEncoderFactory.createDelegatingPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable)
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizationRegistry-> authorizationRegistry
                        .requestMatchers(HttpMethod.GET,"/","/*.html").permitAll()
                        .requestMatchers(
                                "/api/user/**",
                                "/v3/**",
                                "/test/**",
                                "/test2",
                                "/wx/portal/public/**",
                                "/swagger-ui/**",
                                "/templates/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyRequest().authenticated())
                // 禁用缓存
                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.cacheControl(HeadersConfigurer.CacheControlConfig::disable))
                .authenticationProvider(jwtAuthenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // 添加自定义未授权和未登录结果返回
                .exceptionHandling(exceptionConfigurer -> exceptionConfigurer
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(unauthorizedHandler));
        return http.build();
    }
}