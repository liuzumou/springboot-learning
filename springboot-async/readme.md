# springboot async 调用异步方法

> @Async 注解用于执行异步任务，如果从一个线程调用 @Async 方法或类，Spring 会使用另一个线程来运行该方法，从而提高执行效率。

## 使用
- 启动类添加 @EnableAsync 注解
- 在异步方法上加 @Async 注解
- 请求 http://localhost:8080/async
- 查看控制台执行顺序
 
```text
[nio-8080-exec-1] c.e.springboot.async.AsyncController     : 主方法-http-nio-8080-exec-1,start：1713326768275
[nio-8080-exec-1] c.e.springboot.async.AsyncController     : 主方法-http-nio-8080-exec-1,end：1713326768278
[         task-1] c.example.springboot.async.AsyncService  : 异步方法-task-1,start：1713326768279
[         task-1] c.example.springboot.async.AsyncService  : 异步方法-task-1,end：1713326768593
```

## 注意事项

- @Async注解的方法和调用的方法一定不能在同一个类下，也就是说，方法一定要从另一个类中调用，也就是从类的外部调用，类的内部调用是无效的，因为@Transactional和@Async注解的实现都是基于Spring的AOP，而AOP的实现是基于动态代理模式实现的。失败是因为没有经过Spring容器。
- @Async注解的方法的所在类，一定要交给spring容器来管理。用@Component注解(service等)。
- @Async 必须是void 或 Feature 否则报错；
- 异步方法不能为static，这样异步调用不会生效；
- 异步方法必须是public。
- 默认使用 SimpleAsyncTaskExecutor，可以通过实现 AsyncConfigurer 接口来自定义线程池，
- @Async("xxx")，可以使用指定线程池。

## 参考
SpringBoot注解@Async
https://blog.csdn.net/asd051377305/article/details/123898433

@Async注解避坑指南
https://blog.csdn.net/weixin_43764686/article/details/136161117

@Transactional 能和 @Async 一起用吗？
https://springdoc.cn/spring-transactional-async-annotation/
