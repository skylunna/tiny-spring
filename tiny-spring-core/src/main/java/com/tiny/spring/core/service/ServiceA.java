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
public class ServiceA {

    @MiniAutowired("ServiceB")
    private ServiceB serviceB;

    public ServiceA() { System.out.println("1. ServiceA 实例化"); }

    @MiniPostConstruct
    public void init() { System.out.println("3. ServiceA 初始化 (依赖 B: " + (serviceB != null) + ")"); }

    public void say() { System.out.println("A says: " + (serviceB != null ? "B is here" : "B is null")); }
}
