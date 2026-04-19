package com.tiny.spring.core.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: skylunna
 * @data: 2026/3/31
 *
 * @description:
 *      模拟 Spring 的 @Autowired
 */
// 运行时可见
@Retention(RetentionPolicy.RUNTIME)
// 只能用在字段上
@Target(ElementType.FIELD)
public @interface MiniAutowired {
    // 默认空字符串，表示按类型自动匹配
    // 如果指定了值，就按名称精确匹配
    String value() default "";
}
