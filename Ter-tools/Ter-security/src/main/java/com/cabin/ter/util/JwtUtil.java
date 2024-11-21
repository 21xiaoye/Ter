package com.cabin.ter.util;


import cn.hutool.core.date.DateUtil;

import cn.hutool.core.util.StrUtil;
import com.cabin.ter.cache.RedisCache;
import com.cabin.ter.config.JwtConfig;
import com.cabin.ter.constants.RedisKey;
import com.cabin.ter.exception.SecurityException;
import com.cabin.ter.vo.JwtPrincipal;
import com.cabin.ter.vo.UserPrincipal;
import com.cabin.ter.constants.enums.Status;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 *      JWT 工具类
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-04-27 13:06
 */
@EnableConfigurationProperties(JwtConfig.class)
@Component
@Slf4j
public class JwtUtil {
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private RedisCache redisCache;

    /**
     * 创建JWT
     *
     * @param rememberMe    记住我
     * @param userId        用户userId
     * @param subject       用户名
     * @param roleId        用户角色
     * @param authorities   用户权限
     * @return  JWT
     */
    public String createJWT(Boolean rememberMe, Long userId, String subject, Integer roleId, Collection<? extends GrantedAuthority> authorities){
        Date nowTime = new Date();
        JwtBuilder jwtBuilder = Jwts.builder().setId(userId.toString()).setSubject(subject).setIssuedAt(nowTime)
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getKey())
                .claim("role", roleId)
                .claim("authorities", authorities);
        Long ttl = rememberMe ? jwtConfig.getRemember() : jwtConfig.getTtl();

        if(ttl > 0){
            jwtBuilder.setExpiration(DateUtil.offsetMillisecond(nowTime, ttl.intValue()));
        }

        String jwt = jwtBuilder.compact();
        redisCache.set(RedisKey.getKey(RedisKey.REDIS_JWT_KEY_PREFIX, userId), jwt, ttl, TimeUnit.MINUTES);
        return jwt;
    }

    /**
     * 创建JWT
     * @param authentication    用户认证信息
     * @param rememberMe        记住我
     * @return  JWT
     */
    public String createJWT(Authentication authentication, Boolean rememberMe){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return createJWT(rememberMe, userPrincipal.getUserId(), userPrincipal.getUserEmail(),userPrincipal.getRoleId(),userPrincipal.getAuthorities());
    }


    /**
     * 解析JWT
     *
     * @param jwt JWT
     * @return {@link Claims}
     */
    public Claims parseJWT(String jwt) {
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtConfig.getKey()).parseClaimsJws(jwt).getBody();
            String redisKey = RedisKey.getKey(RedisKey.REDIS_JWT_KEY_PREFIX, claims.getId());

            // 校验redis中的JWT是否存在
            Long expire = redisCache.expire(redisKey, TimeUnit.MILLISECONDS);
            if (Objects.isNull(expire) || expire <= 0) {
                throw new SecurityException(Status.TOKEN_EXPIRED);
            }

            // 校验redis中的JWT是否与当前的一致，不一致则代表用户已注销/用户在不同设备登录，均代表JWT已过期
            String redisToken = redisCache.get(redisKey, String.class);
            if (!StrUtil.equals(jwt, redisToken)) {
                throw new SecurityException(Status.TOKEN_OUT_OF_CTRL);
            }
            return claims;
        } catch (ExpiredJwtException e) {
            log.error("Token 已过期");
            throw new SecurityException(Status.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.error("不支持的 Token");
            throw new SecurityException(Status.TOKEN_PARSE_ERROR);
        } catch (MalformedJwtException e) {
            log.error("Token 无效");
            throw new SecurityException(Status.TOKEN_PARSE_ERROR);
        } catch (SignatureException e) {
            log.error("无效的 Token 签名");
            throw new SecurityException(Status.TOKEN_PARSE_ERROR);
        } catch (IllegalArgumentException e) {
            log.error("Token 参数不存在");
            throw new SecurityException(Status.TOKEN_PARSE_ERROR);
        }
    }

    /**
     * 校验token
     * @param jwt 需要校验的token
     * @return  true:token正常,否则false
     */
    public boolean verityToken(String jwt){
        Claims claims = Jwts.parser().setSigningKey(jwtConfig.getKey()).parseClaimsJws(jwt).getBody();

        String redisKey = RedisKey.getKey(RedisKey.REDIS_JWT_KEY_PREFIX, claims.getId());
        // 校验redis中的JWT是否存在
        Long expire = redisCache.expire(redisKey, TimeUnit.MILLISECONDS);
        if (Objects.isNull(expire) || expire <= 0) {
            return false;
        }
        // 校验redis中的JWT是否与当前的一致，不一致则代表用户已注销/用户在不同设备登录，均代表JWT已过期
        String redisToken = redisCache.get(redisKey, String.class);
        if (!StrUtil.equals(jwt, redisToken)) {
            return false;
        }
        return true;
    }
    /**
     * 设置JWT过期
     *
     * @param request 请求
     */
    public void invalidateJWT(HttpServletRequest request) {
        String jwt = getJwtFromRequest(request);
        JwtPrincipal jwtInfo = getJwtInfo(jwt);
        // 从redis中清除JWT
        redisCache.del(RedisKey.getKey(RedisKey.REDIS_JWT_KEY_PREFIX, jwtInfo.getId()));
    }
    public void invalidateJWT(Long userId){
        redisCache.del(RedisKey.getKey(RedisKey.REDIS_JWT_KEY_PREFIX, userId));
    }
    /**
     * 获取jwt中的用户信息
     * @param jwt   用户Jwt
     * @return  用户jwt信息
     */
    public JwtPrincipal getJwtInfo(String jwt){
        Claims claims = parseJWT(jwt);
        return  JwtPrincipal.builder()
                .Id(Long.parseLong(claims.getId()))
                .subject(claims.getSubject())
                .build();
    }
    /**
     * 从 request 的 header 中获取 JWT
     *
     * @param request 请求
     * @return JWT
     */
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StrUtil.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
