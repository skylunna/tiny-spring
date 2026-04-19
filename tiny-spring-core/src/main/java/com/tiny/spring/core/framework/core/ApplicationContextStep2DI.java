package com.tiny.spring.core.framework.core;

import com.tiny.spring.core.framework.annotation.MiniAutowired;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: skylunna
 * @data: 2026/3/31
 *
 * @description:
 *      模拟 Spring 容器  step 2
 */
public class ApplicationContextStep2DI {
    // 1. 用一个 Map 充当容器, 存储管理好的 Bean
    // key 是类名, value 是实例化对象
    private Map<String, Object> beanMap = new HashMap<>();

    public ApplicationContextStep2DI(String... classNames) {
        // 简化: 手动注册一个Bean, 后续改为自动扫描
        // 模拟容器启动时, 根据类名创建对象
        try {
            // 实例化所有 Bean
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                Object bean = clazz.getDeclaredConstructor().newInstance();

                // 按类名注册
                beanMap.put(clazz.getSimpleName(), bean);

                /**
                 * 如果这个类实现了接口，也按接口名注册
                 *
                 * beanMap 中现在会有两个 entry：
                 * "UserServiceImpl" -> UserServiceImpl 实例
                 * "IService" -> UserServiceImpl 实例 ✨
                 * 当注入 IService 类型的字段时，就能找到了！
                 */
                Class<?>[] interfaces = clazz.getInterfaces();
                for (Class<?> iface : interfaces) {
                    beanMap.put(iface.getSimpleName(), bean);
                    System.out.println("注册接口映射: " + iface.getSimpleName() + " -> " + clazz.getSimpleName());
                }
            }

            // 2. 处理依赖注入 (属性赋值)
            // 为什么分两步骤? 因为要确保依赖的Bean已经存在了
            for (Object bean : beanMap.values()) {
                populateBean(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 核心注入逻辑
    private void populateBean(Object bean) throws IllegalAccessException {
        Class<?> clazz = bean.getClass();
        // 1. 获取所有声明的字段 (包括 private)
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            // 2. 检查字段是否有 @MiniAutowired 注解
            if (field.isAnnotationPresent(MiniAutowired.class)) {
                MiniAutowired annotation = field.getAnnotation(MiniAutowired.class);
                // 获取注解指定的名称
                String customBeanName = annotation.value();
                // 3. 获取依赖的类型
                Class<?> fieldType = field.getType();
//                String beanName = fieldType.getSimpleName();

                // 4. 从容器中获取以来的 Bean
                Object dependency = null;

                // 优先使用注解指定的名称
                if (!customBeanName.isEmpty()) {
                    // 如果写了 @MiniAutowired(value = "xxx"), 直接按名称查找
                    dependency = beanMap.get(customBeanName);
                    System.out.println("按名称查找: " + customBeanName);
                } else {
                    // 按照类型查找 (支持接口)
                    dependency = beanMap.get(fieldType.getSimpleName());
                    System.out.println("按类型查找: " + fieldType.getSimpleName());

                    // TODO 如果按类型找到多个怎么办
                    // 可以遍历 beanMap, 用isAssignableFrom 判断
                }

                if (dependency != null) {
                    field.setAccessible(true);
                    field.set(bean, dependency);
                    System.out.println("✅ 注入成功: " + field.getName() + " -> " + dependency.getClass().getSimpleName());
                } else {
                    System.out.println("❌ 注入失败: 找不到合适的 Bean");
                }
            }
        }
    }

    // 5. 提供获取 Bean 的方法 (模拟 getBean)
    public Object getBean(String beanName) {
        return beanMap.get(beanName);
    }
}





















