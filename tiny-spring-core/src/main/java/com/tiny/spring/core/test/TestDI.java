package com.tiny.spring.core.test;

import com.tiny.spring.core.framework.core.ApplicationContextStep2DI;
import com.tiny.spring.core.service.OrderService;

/**
 * @author: skylunna
 * @data: 2026/3/31
 *
 * @description:
 *      模拟 Spring 的依赖注入 DI
 */
public class TestDI {
    public static void main(String[] args) {
        // 注册两个类，注意顺序不重要，因为我们在populateBean前面已经实例化完成了
        ApplicationContextStep2DI context = new ApplicationContextStep2DI(
                "com.tiny.spring.core.service.impl.UserServiceImpl",
                "com.tiny.spring.core.service.impl.AdminServiceImpl",
                "com.tiny.spring.core.service.OrderService"
        );

        OrderService orderService = (OrderService) context.getBean("OrderService");
        orderService.createOrder();
    }
}








