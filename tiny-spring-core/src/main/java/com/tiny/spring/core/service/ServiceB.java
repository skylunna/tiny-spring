package com.tiny.spring.core.service;

import com.tiny.spring.core.framework.annotation.MiniAutowired;
import com.tiny.spring.core.framework.annotation.MiniPostConstruct;

/**
 * @author: skylunna
 * @data: 2026/4/16
 *
 * @description:
 *      依赖循环
 */
public class ServiceB {

    @MiniAutowired("ServiceA")
    private ServiceA serviceA;

    public ServiceB() { System.out.println("2. ServiceB 实例化"); }

    @MiniPostConstruct
    public void init() { System.out.println("4. ServiceB 初始化 (依赖 A: " + (serviceA != null) + ")"); }

    public void say() { System.out.println("B says: " + (serviceA != null ? "A is here" : "A is null")); }
}
