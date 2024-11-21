package com.cabin.ter.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {

    /**
     * 过期时间，默认60s
     * @return
     */
    long expire() default 1 * 60 * 1000;

    /**
     * 缓存标识name
     * @return
     */
    String name() default "";

}