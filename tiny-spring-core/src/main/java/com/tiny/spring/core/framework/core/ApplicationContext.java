package com.tiny.spring.core.framework.core;

import com.tiny.spring.core.framework.annotation.MiniAutowired;
import com.tiny.spring.core.framework.annotation.MiniBefore;
import com.tiny.spring.core.framework.annotation.MiniPostConstruct;
import com.tiny.spring.core.framework.annotation.MiniPreDestroy;
import com.tiny.spring.core.framework.aop.AopProxy;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Supplier;

public class ApplicationContext {
    // 一级缓存：完整 Bean
    private final Map<String, Object> singletonObjects = new HashMap<>();
    // 二级缓存：早期引用（半成品）
    private final Map<String, Object> earlySingletonObjects = new HashMap<>();
    // 三级缓存：Bean 工厂（解决 AOP 代理时机问题）
    private final Map<String, Supplier<Object>> singletonFactories = new HashMap<>();

    // 核心修复：记录 BeanName(简单名) -> 全限定类名 的映射
    private final Map<String, String> beanNameToClassName = new HashMap<>();
    private final List<Object> destroyBeans = new ArrayList<>();

    public ApplicationContext(String... classNames) {
        // 1. 解析传入的全限定类名，建立映射关系
        for (String fullClassName : classNames) {
            try {
                Class<?> clazz = Class.forName(fullClassName);
                String beanName = clazz.getSimpleName();
                // 注册主类名映射
                beanNameToClassName.put(beanName, fullClassName);
                // 注册接口名映射（支持按接口类型注入）
                for (Class<?> iface : clazz.getInterfaces()) {
                    beanNameToClassName.putIfAbsent(iface.getSimpleName(), fullClassName);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Class not found: " + fullClassName, e);
            }
        }

        // 2. 触发所有已注册 Bean 的实例化（解决循环依赖的核心）
        for (String beanName : new ArrayList<>(beanNameToClassName.keySet())) {
            getBean(beanName);
        }

        registerShutdownHook();
    }

    public Object getBean(String beanName) {
        Object bean = getSingleton(beanName);
        if (bean != null) return bean;
        return doCreateBean(beanName);
    }

    // 检查三级缓存
    private Object getSingleton(String beanName) {
        Object bean = singletonObjects.get(beanName);
        if (bean != null) return bean;

        bean = earlySingletonObjects.get(beanName);
        if (bean != null) return bean;

        Supplier<Object> factory = singletonFactories.get(beanName);
        if (factory != null) {
            bean = factory.get();
            earlySingletonObjects.put(beanName, bean);
            singletonFactories.remove(beanName);
        }
        return bean;
    }

    // 实际创建 Bean
    private Object doCreateBean(String beanName) {
        String fullClassName = beanNameToClassName.get(beanName);
        if (fullClassName == null) {
            throw new RuntimeException("Bean definition not found for name: " + beanName);
        }

        try {
            Class<?> clazz = Class.forName(fullClassName);
            // 1. 实例化（调用无参构造）
            Object rawBean = clazz.getDeclaredConstructor().newInstance();

            // 2. 将工厂放入三级缓存，提前暴露引用（打破循环依赖死锁）
            singletonFactories.put(beanName, () -> getEarlyBeanReference(beanName, rawBean));

            // 3. 依赖注入（此时若发生循环依赖，会递归调用 getBean -> 从缓存拿到早期引用）
            populateBean(rawBean);

            // 4. 初始化（@PostConstruct + AOP）
            Object bean = initializeBean(beanName, rawBean);

            // 5. 放入一级缓存，清理二三级
            singletonObjects.put(beanName, bean);
            earlySingletonObjects.remove(beanName);
            singletonFactories.remove(beanName);

            checkPreDestroy(bean);
            return bean;
        } catch (Exception e) {
            throw new RuntimeException("Error creating bean: " + beanName, e);
        }
    }

    private Object getEarlyBeanReference(String beanName, Object rawBean) {
        // 简化版：直接返回原始对象。真实 Spring 会在此处判断并创建 AOP 代理
        return rawBean;
    }

    private void populateBean(Object bean) throws IllegalAccessException {
        Class<?> clazz = bean.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(MiniAutowired.class)) {
                MiniAutowired ann = field.getAnnotation(MiniAutowired.class);
                String targetName = ann.value().isEmpty() ? field.getType().getSimpleName() : ann.value();
                // 递归触发缓存检查
                Object target = getBean(targetName);
                if (target != null) {
                    field.setAccessible(true);
                    field.set(bean, target);
                }
            }
        }
    }

    private Object initializeBean(String beanName, Object bean) {
        // @PostConstruct
        for (Method method : bean.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(MiniPostConstruct.class)) {
                try { method.setAccessible(true); method.invoke(bean); } catch (Exception ignored) {}
            }
        }
        // AOP 代理
        return wrapWithAopIfNecessary(bean);
    }

    private Object wrapWithAopIfNecessary(Object bean) {
        Class<?> clazz = bean.getClass();
        boolean needProxy = false;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MiniBefore.class)) { needProxy = true; break; }
        }
        if (needProxy && clazz.getInterfaces().length > 0) {
            return AopProxy.createProxy(bean);
        }
        return bean;
    }

    private void checkPreDestroy(Object bean) {
        for (Method method : bean.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(MiniPreDestroy.class)) {
                destroyBeans.add(bean);
                break;
            }
        }
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n🛑 容器关闭...");
            for (Object bean : destroyBeans) {
                for (Method method : bean.getClass().getDeclaredMethods()) {
                    if (method.isAnnotationPresent(MiniPreDestroy.class)) {
                        try { method.setAccessible(true); method.invoke(bean); } catch (Exception ignored) {}
                    }
                }
            }
        }));
    }
}