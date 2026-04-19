package com.tiny.spring.core.test;

import com.tiny.spring.core.framework.core.ApplicationContext;
import com.tiny.spring.core.service.ServiceA;
import com.tiny.spring.core.service.ServiceB;

/**
 * @author: skylunna
 * @data: 2026/4/16
 *
 * @description:
 *      依赖循环
 */
public class TestCircularDependency {

    public static void main(String[] args) {
        System.out.println("=== 启动容器（测试循环依赖） ===");
        ApplicationContext context = new ApplicationContext(
                "com.tiny.spring.service.ServiceA",
                "com.tiny.spring.service.ServiceB"
        );

        System.out.println("\n=== 获取 Bean 并调用 ===");
        ServiceA a = (ServiceA) context.getBean("ServiceA");
        ServiceB b = (ServiceB) context.getBean("ServiceB");
        a.say();
        b.say();
    }
}
