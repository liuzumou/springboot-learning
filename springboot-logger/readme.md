# spring boot 中使用和配置日志
springboot 默认采用sl4f + logback 作为日志框架。无需应用外部依赖直接使用。

## 默认使用
```java
private static final Logger logger = LoggerFactory.getLogger(SpringbootLoggerApplication.class);
logger.info("appplication start!");
```

## 配合lombok使用

```java
import lombok.extern.slf4j.Slf4j;

//@Slf4j  在类上加上Slf4j注解
// 使用
log.info("slf4j");
```

## 日志级别

日志级别由低到高分别是：all、trace、debug、info、warn、error、fatal、off。

springboot中默认的日志级别是**info**。默认配置下低于info（trace、debug）的日志将不会输出。

日志级别分为全局和局部，局部日志级别配置将覆盖全局配置。

## logback 配置

logback的配置可在application.yml和xml中配置，其中xml配置更加灵活。
若多个配置同时存在，则其加载顺序为：
logback.xml → application.yml → logback-spring.xml

后加载的会覆盖先加载的。因此application.yml会覆盖logback.xml中的相同配置。即后加载的优先级更高。
同理，若要使用 application.yml 中定义的变量，应使用 logback-spring.xml 。官方推荐使用 logback-spring.xml 。

**application.yml**
```yml
#日志配置
logging:
  config: classpath:logback-spring.xml  #指定项目启动的时，读取logback-spring.xml日志配置文件
  level: #配置指定包的路径下应用程序的日志记录和日志级别。
    root: info
    org.springframework: warn
```

**logback-spring.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <property name="log.path" value="/path/logs"/>
    <property name="console.log.pattern"
              value="%red(%d{yyyy-MM-dd HH:mm:ss}) %green([%thread]) %highlight(%-5level) %boldMagenta(%logger{36}%n) - %msg%n"/>
    <property name="log.pattern" value="%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"/>

    <!-- 控制台输出 -->
    <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${console.log.pattern}</pattern>
            <charset>utf-8</charset>
        </encoder>
    </appender>

    <!-- 控制台输出 -->
    <appender name="file_console" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${log.path}/sys-console.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- 日志文件名格式 -->
            <fileNamePattern>${log.path}/sys-console.%d{yyyy-MM-dd}.log</fileNamePattern>
            <!-- 日志最大 1天 -->
            <maxHistory>1</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>${log.pattern}</pattern>
            <charset>utf-8</charset>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <!-- 过滤的级别 -->
            <level>INFO</level>
        </filter>
    </appender>

    <!-- 系统日志输出 -->
    <appender name="file_info" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${log.path}/sys-info.log</file>
        <!-- 循环政策：基于时间创建日志文件 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- 日志文件名格式 -->
            <fileNamePattern>${log.path}/sys-info.%d{yyyy-MM-dd}.log</fileNamePattern>
            <!-- 日志最大的历史 60天 -->
            <maxHistory>60</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>${log.pattern}</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <!-- 过滤的级别 -->
            <level>INFO</level>
            <!-- 匹配时的操作：接收（记录） -->
            <onMatch>ACCEPT</onMatch>
            <!-- 不匹配时的操作：拒绝（不记录） -->
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <appender name="file_error" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${log.path}/sys-error.log</file>
        <!-- 循环政策：基于时间创建日志文件 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- 日志文件名格式 -->
            <fileNamePattern>${log.path}/sys-error.%d{yyyy-MM-dd}.log</fileNamePattern>
            <!-- 日志最大的历史 60天 -->
            <maxHistory>60</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>${log.pattern}</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <!-- 过滤的级别 -->
            <level>ERROR</level>
            <!-- 匹配时的操作：接收（记录） -->
            <onMatch>ACCEPT</onMatch>
            <!-- 不匹配时的操作：拒绝（不记录） -->
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <!-- info异步输出 -->
    <appender name="async_info" class="ch.qos.logback.classic.AsyncAppender">
        <!-- 不丢失日志.默认的,如果队列的80%已满,则会丢弃TRACT、DEBUG、INFO级别的日志 -->
        <discardingThreshold>0</discardingThreshold>
        <!-- 更改默认的队列的深度,该值会影响性能.默认值为256 -->
        <queueSize>512</queueSize>
        <!-- 添加附加的appender,最多只能添加一个 -->
        <appender-ref ref="file_info"/>
    </appender>

    <!-- error异步输出 -->
    <appender name="async_error" class="ch.qos.logback.classic.AsyncAppender">
        <!-- 不丢失日志.默认的,如果队列的80%已满,则会丢弃TRACT、DEBUG、INFO级别的日志 -->
        <discardingThreshold>0</discardingThreshold>
        <!-- 更改默认的队列的深度,该值会影响性能.默认值为256 -->
        <queueSize>512</queueSize>
        <!-- 添加附加的appender,最多只能添加一个 -->
        <appender-ref ref="file_error"/>
    </appender>

    <!--系统操作日志-->
    <root level="info">
        <appender-ref ref="console" />
        <appender-ref ref="async_info" />
        <appender-ref ref="async_error" />
        <appender-ref ref="file_console" />
    </root>

</configuration>
```

## 参考
https://blog.csdn.net/Trong_/article/details/132351238 

https://blog.csdn.net/fyyyr/article/details/131918634