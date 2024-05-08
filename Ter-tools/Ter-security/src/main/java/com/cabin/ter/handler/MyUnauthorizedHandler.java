package com.cabin.ter.handler;

import com.cabin.ter.constants.enums.Status;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p>
 *     未登录、未认证时回调
 * </p>
 * @author xiaoye
 * @data Created in 2024-04-27 10:37
 */
@Slf4j
@Component
public class MyUnauthorizedHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(Status.UNAUTHORIZED);
        response.getWriter().flush();
    }
}
