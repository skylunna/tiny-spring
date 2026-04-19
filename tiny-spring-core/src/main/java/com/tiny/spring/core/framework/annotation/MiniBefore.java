package com.tiny.spring.core.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: skylunna
 * @data: 2026/04/01
 *
 * @description:
 *      模拟 Spring 的 AOP
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MiniBefore {
    String value() default "";
}
