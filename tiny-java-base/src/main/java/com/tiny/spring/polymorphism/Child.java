package com.tiny.spring.polymorphism;

/**
 * Copyright (c) 2026 skyluna. All Rights Reserved.
 *
 * @Author: skylunna
 * @Date: 2026/4/27 16:45
 * @Description: Child 类功能描述
 */
public class Child extends Parent {
    String field = "Child-Field";

    static String staticField = "Child-Static";

    @Override
    void show() {
        System.out.println("Child.show() | field = " + field);
    }

    static void staticShow() {
        System.out.println("Child.staticShow() | staticField = " + staticField);
    }
}