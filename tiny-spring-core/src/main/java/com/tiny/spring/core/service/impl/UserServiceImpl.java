package com.tiny.spring.core.service.impl;

import com.tiny.spring.core.service.IService;

/**
 * @author: skylunna
 */
public class UserServiceImpl implements IService {
    // 模拟我们平时写的业务代码

    public UserServiceImpl() {
        System.out.println("UserServiceImpl 被实例化了...");
    }

    @Override
    public void sayHello() {
        System.out.println("Hello, tiny-spring");
    }

    @Override
    public void createOrder() {
        System.out.println("UserServiceImpl的createOrder");
    }
}
