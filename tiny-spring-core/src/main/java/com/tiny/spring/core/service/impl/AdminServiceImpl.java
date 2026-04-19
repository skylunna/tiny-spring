package com.tiny.spring.core.service.impl;

import com.tiny.spring.core.service.IService;

/**
 * @author: skylunna
 * @data: 2026/3/31
 *
 * @description:
 *      AdminServiceImpl
 */
public class AdminServiceImpl implements IService {

    @Override
    public void sayHello() {
        System.out.println("Hello, I'm AdminServiceImpl.");
    }

    @Override
    public void createOrder() {
        System.out.println("AdminServiceImpl 的 createOrder.");
    }
}
