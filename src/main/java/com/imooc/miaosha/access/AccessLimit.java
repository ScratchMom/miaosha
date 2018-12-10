package com.imooc.miaosha.access;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author laowang
 * @date 2018/12/6 6:51 PM
 * @Description:
 */
    @Retention(RUNTIME)
    @Target(ElementType.METHOD)
    public @interface AccessLimit {
        int seconds();
        int maxCount();
        boolean needLogin() default true;
    }
