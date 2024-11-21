package com.cabin.ter.aop;

import com.alibaba.fastjson.JSON;
import com.cabin.ter.annotation.Cache;
import com.cabin.ter.cache.RedisCache;
import com.cabin.ter.constants.enums.Status;
import com.cabin.ter.constants.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Component
@Aspect
@Slf4j
public class CacheAspect {

    @Autowired
    private RedisCache redisCache;
    /**
     * 缓存操作
     *
     * @param pjp
     * @return
     */
    @Around("@annotation(com.cabin.ter.annotation.Cache)")
    public Object toCache(ProceedingJoinPoint pjp) {

        try {
            Signature signature = pjp.getSignature();
            String className = pjp.getTarget().getClass().getSimpleName();
            String methodName = signature.getName();
            Object[] args = pjp.getArgs();
            Class[] parameterTypes = new Class[args.length];
            StringBuilder params = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null) {
                    parameterTypes[i] = args[i].getClass();
                    params.append(JSON.toJSONString(args[i]));
                }
            }
            if (StringUtils.isNotEmpty(params.toString())) {
                params = new StringBuilder(DigestUtils.md5Hex(params.toString()));
            }
            @SuppressWarnings("unchecked")
            Method method = signature.getDeclaringType().getMethod(methodName, parameterTypes);

            Cache annotation = method.getAnnotation(Cache.class);
            long expire = annotation.expire();
            String name = annotation.name();

            String redisKey = name + "::" + className + "::" + methodName + "::" + params;
            ApiResponse result = redisCache.get(redisKey, ApiResponse.class);
            if (Objects.nonNull(result)) {
                log.info("数据从redis缓存中获取,key: {}", redisKey);
                return result;
            }
            Object proceed = pjp.proceed();
            redisCache.set(redisKey, proceed, expire, TimeUnit.MILLISECONDS);
            log.info("数据存入redis缓存,key: {}", redisKey);
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return  ApiResponse.ofStatus(Status.ERROR);
    }

}