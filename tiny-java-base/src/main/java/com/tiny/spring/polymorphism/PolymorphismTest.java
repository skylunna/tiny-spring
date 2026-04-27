package com.tiny.spring.polymorphism;

/**
 * Copyright (c) 2026 skyluna. All Rights Reserved.
 *
 * @Author: skylunna
 * @Date: 2026/4/27 16:47
 * @Description: PolymorphismTest 类功能描述
 */
public class PolymorphismTest {
    public static void main(String[] args) {
        Parent p = new Child();

        System.out.println("--- 字段访问（静态绑定） ---");
        System.out.println(p.field);
        System.out.println(p.staticField);

        System.out.println("\n--- 方法调用（动态绑定） ---");
        // Child.show()
        p.show();
        // Parent.staticShow()
        p.staticShow();

        System.out.println("\n--- 强制类型转换 ---");
        // Child.show() (引用类型变了，但对象还是 Child)
        ((Child) p).show();
    }
}