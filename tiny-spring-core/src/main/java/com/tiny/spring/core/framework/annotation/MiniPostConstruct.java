package com.tiny.spring.core.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: skylunna
 * @data: 2026/04/02
 *
 * @description:
 *      Bean的生命周期 依赖注入完成后执行 初始化方法
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MiniPostConstruct {
}
