package com.cabin.ter.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import io.jsonwebtoken.*;

import java.net.URLEncoder;
import java.util.Date;

public class TemporaryLinkUtil {
    private static final String PUBLIC_URL = "http://192.168.92.254:6771";
    private static final String SECRET_KEY = "dnqwdbwiudwhduw";
    private static final Integer VALID_TIME =  5 * 60 * 1000;
    private static final Snowflake snowflake = new Snowflake();
    private static final String USER_EMAIL_BINDING_URL = "%s/emailBindingPage?token=%s&openId=%s";
    /**
     * 生成临时邮箱绑定链接
     * @return  临时邮箱绑定链接
     */
    public static String buildTemporaryUserEmailBindingLink(String openId){
        String token = temporaryJWT();
        return String.format(USER_EMAIL_BINDING_URL,PUBLIC_URL, token,openId);
    }

    /**
     * 生成一个临时token，用来判断链接是否过期
     * @return  返回生成的临时token
     */
    private static String temporaryJWT(){
        Date nowTime = new Date();
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId(String.valueOf(snowflake.nextId()))
                .setIssuedAt(nowTime)
                .setExpiration(new Date(nowTime.getTime() + VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY);

        return jwtBuilder.compact();
    }

    /**
     * 校验token有没有过期
     * @param token 需要检验的token
     * @return  true没有过期 false token过期不可用
     */
    public static boolean isTokenExpired(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
