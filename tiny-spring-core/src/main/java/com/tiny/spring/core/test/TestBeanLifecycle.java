package com.tiny.spring.core.test;

import com.tiny.spring.core.framework.core.ApplicationContextStep4BeanLifeCycle;
import com.tiny.spring.core.service.OrdersService;

/**
 * @author: skylunna
 * @data: 2026/04/02
 *
 * @description:
 *      测试 Bean 的生命周期
 */
public class TestBeanLifecycle {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 启动容器 ===");
        ApplicationContextStep4BeanLifeCycle context = new ApplicationContextStep4BeanLifeCycle(
                "com.tiny.spring.service.impl.UserServiceImpl",
                "com.tiny.spring.service.impl.OrderServiceImpl"
        );

        System.out.println("\n=== 使用 Bean ===");
        OrdersService ordersService = (OrdersService) context.getBean("OrdersService");
        ordersService.createOrder();

        System.out.println("\n=== 模拟程序运行 ===");
        // 必须让主线程存活一会儿, 否则 JVM 直接退出, 看不到销毁逻辑
        // 或者手动关闭 System.exit(0) 也会触发 shutdown hook
        Thread.sleep(2000);
        System.out.println("\n=== 主程序结束 ===");
    }
}
