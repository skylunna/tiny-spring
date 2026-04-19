package com.tiny.spring.core.service.impl;

import com.tiny.spring.core.framework.annotation.MiniAutowired;
import com.tiny.spring.core.framework.annotation.MiniBefore;
import com.tiny.spring.core.framework.annotation.MiniPostConstruct;
import com.tiny.spring.core.framework.annotation.MiniPreDestroy;
import com.tiny.spring.core.service.IService;
import com.tiny.spring.core.service.OrdersService;

public class OrderServiceImpl implements OrdersService {

    @MiniAutowired
    private IService userService;

    public OrderServiceImpl() {
        System.out.println("1. 构造函数： OrderServiceImpl 实例化");
    }

    @MiniPostConstruct
    public void init() {
        System.out.println("3. 初始化：@PostConstruct (此时 userService 已注入)");
        // 验证DI是否完成
        if (userService != null) {
            System.out.println("   验证：userService 不为 null ✅");
        }
    }

    @MiniPreDestroy
    public void destroy() {
        System.out.println("5. 销毁：@PreDestroy");
    }

    @MiniBefore("2. AOP 前置增强")
    @Override
    public void createOrder() {
        System.out.println("4. 业务：执行订单逻辑");
    }

    @Override
    public void sayHello() {
        System.out.println("Hello, I'm OrderServiceImpl.");
    }
}
