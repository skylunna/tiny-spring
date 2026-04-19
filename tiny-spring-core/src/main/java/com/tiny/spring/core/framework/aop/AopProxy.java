package com.tiny.spring.core.framework.aop;

import com.tiny.spring.core.framework.annotation.MiniBefore;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author: skylunna
 * @data: 2026/04/01
 *
 * @description:
 *      模拟 Spring 的 AOP 代理
 */
public class AopProxy {

    // 保存目标对象，用于查找实现类上的注解
    private static Object targetObject;

    // 创建代理对象
    public static Object createProxy(Object target) {
//        System.out.println("🔥 [T1] 开始创建代理对象...");
        // 保存目标对象
        targetObject = target;

        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                        System.out.println("🔥 [T2]: invoke()被触发! 方法: " + method.getName());
//                        System.out.println("🐞 [Debug]: 进入 invoke()方法");
//                        System.out.println("🐞 [Debug]: 方法名: " + method.getName());
//                        System.out.println("🐞 [Debug]: 目标对象: " + target.getClass().getSimpleName());

                        // 现在接口方法上查找注解，找不到再去实现类上找
                        MiniBefore annotation = getAnnotation(method);
                        if (annotation != null) {
                            System.out.println("🔹 " + annotation.value());
                        }

                        // 执行原方法
                        Object result = method.invoke(target, args);
                        //TODO 后置增强 用于@MiniAfter
                        return result;
                    }

                    // 递归查找注解 (接口 -> 实现类)
                    private MiniBefore getAnnotation(Method interfaceMethod) {
                        // 1. 先检查接口方法本身
                        if (interfaceMethod.isAnnotationPresent(MiniBefore.class)) {
                            return interfaceMethod.getAnnotation(MiniBefore.class);
                        }

                        // 2. 去实现类上找对应的方法
                        try {
                            Method implMethod = targetObject.getClass()
                                    .getDeclaredMethod(interfaceMethod.getName(),
                                            interfaceMethod.getParameterTypes());
                            if (implMethod.isAnnotationPresent(MiniBefore.class)) {
                                return implMethod.getAnnotation(MiniBefore.class);
                            }
                        } catch (NoSuchMethodException e) {
                            // 实现类没有这个方法，忽略
                        }

                        return null;
                    }
                }

        );
    }
}















