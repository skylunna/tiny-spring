## Base
### Spring解决了什么问题
### 为什么要用Bean，直接new对象会怎么样

## IoC
### 关于反射性能
- 我们在代码里用了 Class.forName 和 newInstance。你觉得这种方式比直接 new UserServiceImpl() 慢吗？为什么？

### 关于 IoC（控制反转）：
- 在 Test.java 中，你并没有写 new UserServiceImpl()。对象是谁创建的？

- 如果明天你要把 UserServiceImpl 换成 UserServiceImplV2，你需要改几处代码？（对比传统开发模式）。

---

## DI
### field.setAccessible(true) 是做什么的?
- 如果不写这一行，代码会报错什么?  (IllegalAccessException)
- 为什么 Spring 可以注入 private 字段?
- 暴力反射会不会有安全风险? 会不会影响性能?

### 实例化与注入的顺序
- ApplicationContext中分两步，先newInstance所有Bean，再populateBean所有Bean
- 为什么要分两步？如果边实例化边注入会发生什么问题? (如果A依赖B，B还没创建好怎么办?)
- **循环依赖**:
    * 如果UserService也依赖OrderService，现在的代码能成功吗？
    * 会发生什么异常？（通常是NullPointerException）或者无限递归
    * ! 这是Spring三级缓存要解决的问题

---

### AOP
- JDK动态代理和CGLIB有什么区别

| 特性                 | JDK动态代理             | CGLIB      |
|--------------------|---------------------|------------|
| **要求**             | 目标类必须实现接口           | 无要求，继承目标类  |
| **原理**             | 反射 + InvocationHandler | 字节码生成（ASM） |
| **性能**             | 略慢（反射调用）            | 略快（直接调用）   |
| **Spring默认**       | Spring Boot 2.x+ 默认优先 JDK         | 可配置 proxy-target-class=true 强制 CGLIB  |


- 为什么同一个类中，方法A调用方法B，B上的AOP不生效?
  * 因为AOP代理的是**外部调用**。类内部```this.methodB()```是直接调用，没经过代理对象。
  * 可以通过AopContext.currentProxy()获取代理对象再调用
- @MiniBefore 注解标在接口方法和实现类方法上，哪个生效?
  * 默认实现类优先，Spring会先检查实现类，再查接口。可以在wrapWithAop中添加逻辑:
```method.getAnnotation()```找不到时，用```method.getDeclaringClass().getMethod(...).getAnnotatioin()```查接口

- ```Proxy.newProxyInstance```要求目标类实现接口，但我的Service没有实现接口怎么办?
  * 这是JDK代理的限制。
  * 让Service实现接口 ⭐
  * 改用CGLIB代理，引入cglib依赖，用Enhancer类
- 多个```@MiniBefore```注解，执行顺序是什么?
  * ```method.getAnnotations()``` 返回的顺序不确定。如果需要控制顺序，可以给注解加```order()```属性，然后用```Arrays.sort()```排序

- 代理对象的class名字为什么是```$Proxy0```
  * 这是JDK动态代理生成的代理类名称。可以用```proxy.getClass().getSuperclass()```查看父类，用
```proxy.getClass().getInterfaces()``` 查看实现的接口

---

### Bean Lifecycle
- Spring Bean 生命周期的核心步骤是什么？
  * 实例化 (Instantiation)：```new``` 对象。
  * 属性赋值 (Populate)：依赖注入 (```@Autowired```)。
  * 初始化 (Initialization)：```@PostConstruct```, ```InitializingBean.afterPropertiesSet```, ```init-method```。
  * 使用 (Usage)：业务调用。
  * 销毁 (Destruction)：```@PreDestroy```, ```DisposableBean.destroy```, ```destroy-method```。

-  ```@PostConstruct``` 和 ```InitializingBean``` 有什么区别？执行顺序？
  * ```@PostConstruct``` 是 JSR-250 标准注解，Spring 支持。
  * ```InitializingBean``` 是 Spring 专属接口。
  * 顺序：```@PostConstruct``` -> ```InitializingBean.afterPropertiesSet``` -> 自定义 ```init-method```。
  * 建议：优先用 ```@PostConstruct```，耦合度低。

- 为什么 ```@PostConstruct``` 里可以使用注入的 Bean，但构造函数里不行？
  * 因为执行顺序是 先实例化 (构造函数) -> 再属性赋值 (DI) -> 最后初始化 (```@PostConstruct```)。构造函数执行时，依赖还没塞进来呢！