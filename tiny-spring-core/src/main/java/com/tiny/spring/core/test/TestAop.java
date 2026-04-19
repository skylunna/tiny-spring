package com.tiny.spring.core.test;

import com.tiny.spring.core.framework.core.ApplicationContextStep3Aop;
import com.tiny.spring.core.service.OrdersService;

/**
 * @author: skylunna
 * @data: 2026/04/01
 *
 * @description:
 *      测试 Spring 的 Aop
 */
public class TestAop {
    public static void main(String[] args) {
        ApplicationContextStep3Aop context = new ApplicationContextStep3Aop(
                "com.tiny.spring.core.service.impl.OrderServiceImpl"
        );

        // 注意: getBean 返回的可能是代理对象
        OrdersService orderService = (OrdersService) context.getBean("OrdersService");
        System.out.println("=== 调用带 AOP 的方法 ===");
        orderService.createOrder();

        System.out.println("\n=== 调用普通方法 ===");
        orderService.sayHello();
    }
}
