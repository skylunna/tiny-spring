package com.tiny.spring.core.framework.core;

import com.tiny.spring.core.framework.annotation.MiniAutowired;
import com.tiny.spring.core.framework.annotation.MiniBefore;
import com.tiny.spring.core.framework.annotation.MiniPostConstruct;
import com.tiny.spring.core.framework.annotation.MiniPreDestroy;
import com.tiny.spring.core.framework.aop.AopProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: skylunna
 * @data: 2026/04/02
 *
 * @description:
 *      模拟 Spring 容器 step 4 Bean的生命周期
 */
public class ApplicationContextStep4BeanLifeCycle {
    // 1. 用一个 Map 充当容器, 存储管理好的 Bean
    // key 是类名, value 是实例化对象
    private Map<String, Object> beanMap = new HashMap<>();

    // 记录要销毁的 Bean
    private List<Object> destoryBeans = new ArrayList<>();

    public ApplicationContextStep4BeanLifeCycle(String... classNames) {
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
//                    System.out.println("注册接口映射: " + iface.getSimpleName() + " -> " + clazz.getSimpleName());
                }
            }

            // 2. 处理依赖注入 (属性赋值)
            // 为什么分两步骤? 因为要确保依赖的Bean已经存在了
            for (Object bean : beanMap.values()) {
                populateBean(bean);
            }

            // 3. 新增，初始化所有Bean (@PostConstruct)
            for (Object bean : beanMap.values()) {
                invokeInitMethod(bean);
            }

            // 4. 新增，注册销毁狗子 (JVM 关闭时触发)
            registerShutdownHook();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 新增，初始化方法
    private void invokeInitMethod(Object bean) {
        Class<?> clazz = bean.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MiniPostConstruct.class)) {
                try {
                    method.setAccessible(true);
                    // 通过反射调用方法
                    method.invoke(bean);
//                    System.out.println("🌱 初始化完成：" + clazz.getSimpleName() + "." + method.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 注册 JVM 关闭钩子
    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n🛑 容器正在关闭...");
            for (Object bean : destoryBeans) {
                invokeDestroyMethods(bean);
            }
        }));
    }

    // 销毁方法
    private void invokeDestroyMethods(Object bean) {
        Class<?> clazz = bean.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MiniPreDestroy.class)) {
                try {
                    method.setAccessible(true);
                    method.invoke(bean);
                    System.out.println("🍂 销毁完成：" + clazz.getSimpleName() + "." + method.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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

        // 检查是否有销毁注解
        for (Method method : bean.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(MiniPreDestroy.class)) {
                destoryBeans.add(bean);
                break;
            }
        }
    }

    // 5. 提供获取 Bean 的方法 (模拟 getBean)
    public Object getBean(String beanName) {
        Object bean = beanMap.get(beanName);
        if (bean == null) {
            return null;
        }

        // AOP 增强 如果 Bean 中带有 @MiniBefore 的方法，返回代理对象
        return wrapWithAop(bean);
    }

    // 判断是否需要代理，并创建代理
    private Object wrapWithAop(Object bean) {
        Class<?> clazz = bean.getClass();

        // 1. 检查是否有方法带@MiniBefore注解
        boolean needProxy = false;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MiniBefore.class)) {
                needProxy = true;
                break;
            }
        }

        // 2. 如果需要代理，且目标类实现了接口，用JDK动态代理
        if (needProxy && clazz.getInterfaces().length > 0) {
            System.out.println("🔧 创建 AOP 代理: " + clazz.getSimpleName());
            return AopProxy.createProxy(bean);
        }

        // 3. 否则返回原对象
        return bean;
    }
}
