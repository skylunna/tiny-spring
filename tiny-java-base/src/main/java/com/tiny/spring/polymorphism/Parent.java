package com.tiny.spring.polymorphism;

/**
 * Copyright (c) 2026 skyluna. All Rights Reserved.
 *
 * @Author: skylunna
 * @Date: 2026/4/27 16:42
 * @Description: Parent 类功能描述
 */
public class Parent {
    String field = "Parent-Field";

    static String staticField = "Parent-Static";

    void show() {
        System.out.println("Parent.show()");
    }

    static void staticShow() {
        System.out.println("Parent.staticShow()");
    }

    Parent() {
        System.out.println("Parent 构造器调用 show(): ");
        // ⚠️ 多态陷阱
        show();
    }
}