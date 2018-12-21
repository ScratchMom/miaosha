package com.imooc.miaosha.validator;

import org.springframework.transaction.annotation.Transactional;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author yifan
 * @date 2018/7/20 下午1:52
 * 自定义的注解
 * Jsr303参数校验框架
 * @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER }) 设置注解权限
 *
 * 1.	CONSTRUCTOR:用于描述构造器
 * 2.	FIELD:用于描述域
 * 3.	LOCAL_VARIABLE:用于描述局部变量
 * 4.	METHOD:用于描述方法
 * 5.	PACKAGE:用于描述包
 * 6.	PARAMETER:用于描述参数
 * 7.	TYPE:用于描述类、接口(包括注解类型) 或enum声明
 *
 * 2.@Retention
 * 表示需要在什么级别保存该注释信息，用于描述注解的生命周期（即：被描述的注解在什么范围内有效）
 */

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {IsMobileValidator.class}) // 系统看到这个注解会调用这个验证器去校验
public @interface IsMobile {

    boolean required() default true;

    String message() default "手机号码格式错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
