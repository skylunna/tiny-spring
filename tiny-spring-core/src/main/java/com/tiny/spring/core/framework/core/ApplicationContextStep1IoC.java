package com.tiny.spring.core.framework.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: skylunna
 * @data: 2026/3/30
 *
 * @description:
 *      模拟 Spring 容器 step 1
 */
public class ApplicationContextStep1IoC {

    // 1. 用一个 Map 充当容器, 存储管理好的 Bean
    // key 是类名, value 是实例化对象
    private Map<String, Object> beanMap = new HashMap<>();

    public ApplicationContextStep1IoC(String packageName) {
        // 简化: 手动注册一个Bean, 后续改为自动扫描
        // 模拟容器启动时, 根据类名创建对象
        try {
            // 2. 加载类, 反射第一步
            // 假设要创建 UserServiceImpl 类
            /**
             *  xxx.xxx.xxxx.UserServiceImpl 是 "全限定类名"
             *  xxx/xxx/xxx/ 是 Linux的路径名
             *  xxx\xxx\xxx\ 是 Windows的路径名
             *  Java 为了跨平台，屏蔽了系统差异
             *
             *  Class<?> clazz = Class.forName(className);
             *      1. 类加载
             *          当JVM执行到这里时，会发现，我要用这个类，但我还没有把它加载到内存里
             *          于是，类加载器(ClassLoader):
             *              - 根据类名找到对应的 UserServiceImpl.class 文件
             *              - 把文件里的二进制数据读入内存
             *              - 进行验证、准备、解析、初始化
             *      2. 创建 Class 对象 (反射的入口)
             *          加载完成后，JVM会在堆内存中创建一个 java.lang.Class 类型的对象
             *          注意：
             *              - 这里创建的不是 UserServiceImpl对象, 而是描述 UserServiceImpl这个类的对象
             *              - 里面包含了 UserServiceImpl 的所有 "元数据": 有哪些方法、有哪些字段、有哪些构造器、继承自谁、实现了什么接口
             */
            String className = "com.tiny.spring.service.impl.UserServiceImpl";
            Class<?> clazz = Class.forName(className);

            // 3. 创建实例, 反射第二步
            // 不要用 clazz.newInstance(), 已废弃
            // 要用构造器方式
            Object bean = clazz.getDeclaredConstructor().newInstance();

            // 4. 存入容器
            beanMap.put(clazz.getSimpleName(), bean);

            System.out.println("Bean 注册成功: " + clazz.getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 5. 提供获取 Bean 的方法 (模拟 getBean)
    public Object getBean(String beanName) {
        return beanMap.get(beanName);
    }
}
