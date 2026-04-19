package com.tiny.spring.core.service;

import com.tiny.spring.core.framework.annotation.MiniAutowired;
import com.tiny.spring.core.framework.annotation.MiniBefore;

/**
 * @author: skylunna
 * @data: 2026/3/31
 *
 * @description:
 *      MiniAutowired 依赖方法
 */
public class OrderService implements IService {

    // 1. 声明依赖，加上我们的注解
    @MiniAutowired("UserServiceImpl")
    private IService iService;

    @MiniBefore("🔍 [AOP] 权限校验通过")
    public void createOrder() {
        // 使用依赖
        if (iService != null) {
            iService.sayHello();
            System.out.println("Order created successfully.");
        } else {
            System.out.println("Error: userService is null! Injection failed.");
        }
    }

    @Override
    public void sayHello() {
        System.out.println("Hello, I'm OrderService.");
    }
}

















