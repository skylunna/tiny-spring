package com.tiny.spring.core.test;

import com.tiny.spring.core.framework.core.ApplicationContextStep2DI;
import com.tiny.spring.core.service.IService;

/**
 * @author: skylunna
 * @data: 2026/3/31
 *
 * @description:
 *      测试控制反转 IoC
 */
public class TestIoC {
    public static void main(String[] args) {
        // 1. 启动容器
        ApplicationContextStep2DI context = new ApplicationContextStep2DI("com.tiny.spring.service");

        // 2. 从容器中获取Bean
        IService service = (IService) context.getBean("UserServiceImpl");

        // 3. 使用 Bean
        service.sayHello();
    }
}
