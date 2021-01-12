# 1. 异步执行
###### ⏰ 关键代码<span id="关键代码"></span>
---
- ⭕🌏 使用 `@EnableAsync` 注解开启SpringBoot的异步支持
    ```java
    @SpringBootApplication
    @EnableAsync
    public class DemoAsyncApplication {
        public static void main(String[] args) {
            SpringApplication.run(DemoAsyncApplication.class, args);
        }
    }
    ```
- ⭕🌏 在需要异步执行的代码上使用 `@Async` 注解，应用会使用另一个线程执行方法
    ```java
    @Async
    public Future<String> asyncTask1() throws InterruptedException {
            log.info("异步方法 asyncTask1 开始执行，执行线程为{}",  Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(10);
            log.info("异步方法 asyncTask1 结束执行，执行线程为{}",  Thread.currentThread().getName());
            return  new AsyncResult<>("this is asyncTask1");
        }
    ```

###### ⏰ Maven依赖<span id="Maven依赖"></span>
 ---
 - ⭕🌏 pom.xml 中使用如下依赖，如下：
```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
        </dependency>
        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
        </dependency>
    </dependencies>
```
>  `spring-boot-starter` 引入的包，包含了异步执行的功能，其余的都是工具包。
---
###### ⏰ 测试主体代码<span id="测试主体代码"></span>
- ⭕🌏 程序中我们创建 `AsyncTask` 测试类，模拟执行异步方法和同步方法。如下：
    ```java
    @Service
    @Slf4j
    public class AsyncTask {
        //------------------ 创建不同的异步任务，模拟方法执行 ----------------------------------------
        @Async
        public Future<String> asyncTask1() throws InterruptedException {
            log.info("异步方法 asyncTask1 开始执行，执行线程为{}",  Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(10);
            log.info("异步方法 asyncTask1 结束执行，执行线程为{}",  Thread.currentThread().getName());
            return  new AsyncResult<>("this is asyncTask1");
        }
        @Async
        public Future<String> asyncTask2() throws InterruptedException {
            log.info("异步方法 asyncTask2 开始执行，执行线程为{}",  Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(7);
            log.info("异步方法 asyncTask2 结束执行，执行线程为{}",  Thread.currentThread().getName());
            return  new AsyncResult<>("this is asyncTask2");
        }
        @Async
        public Future<String> asyncTask3() throws InterruptedException {
            log.info("异步方法 asyncTask3 开始执行，执行线程为{}",  Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(1);
            log.info("异步方法 asyncTask3 结束执行，执行线程为{}",  Thread.currentThread().getName());
            return  new AsyncResult<>("this is asyncTask3");
        }
        //------------------ 创建不同的同步任务，模拟方法执行 ----------------------------------------
        public String syncTask1() throws InterruptedException {
            log.info("同步方法 asyncTask1 开始执行，执行线程为{}",  Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(10);
            log.info("同步方法 asyncTask1 结束执行，执行线程为{}",  Thread.currentThread().getName());
            return  "this is syncTask1";
        }
        public String syncTask2() throws InterruptedException {
            log.info("同步方法 syncTask2 开始执行，执行线程为{}",  Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(7);
            log.info("同步方法 syncTask2 结束执行，执行线程为{}",  Thread.currentThread().getName());
            return  "this is syncTask2";
        }
        public String syncTask3() throws InterruptedException {
            log.info("同步方法 syncTask3 开始执行，执行线程为{}", Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(1);
            log.info("同步方法 syncTask3 结束执行，执行线程为{}",  Thread.currentThread().getName());
            return  "this is syncTask3";
        }
    }
    ```
###### ⏰ 测试代码<span id="测试代码"></span>
- 如下范例所示：
    ```java
    @Slf4j
    @SpringBootTest
    class AsyncTaskTest {
        @Autowired
        private AsyncTask asyncTask;
        @Test
        public void asyncTaskTest() throws InterruptedException, ExecutionException {
            Stopwatch started = Stopwatch.createStarted();
            Future<String> stringFuture1 = asyncTask.asyncTask1();
            Future<String> stringFuture2 = asyncTask.asyncTask2();
            Future<String> stringFuture3 = asyncTask.asyncTask3();
            // get() 会阻塞当前 main 线程 等待返回再向下执行
            log.info("{}, 执行完成", stringFuture1.get());
            log.info("{}, 执行完成", stringFuture3.get());
            log.info("{}, 执行完成", stringFuture2.get());
            log.info("异步任务执行完成共: {} 毫秒", started.stop().elapsed(TimeUnit.MILLISECONDS));
        }
        @Test
        public void syncTaskTest() throws InterruptedException {
            Stopwatch started = Stopwatch.createStarted();
            asyncTask.syncTask1();
            asyncTask.syncTask2();
            asyncTask.syncTask3();
            log.info("同步任务执行完成共: {} 毫秒", started.stop().elapsed(TimeUnit.MILLISECONDS));
        }
    }
    ```
    >  使用 `get()` 阻塞当前线程，等待异步方法的结果返回。
---    
###### ⏰ 执行单元测试<span id="执行单元测试"></span>
- 执行测试代码，打印日志如下：
```sh
     [           main] cn.ohbee.demoasync.AsyncTask             : 同步方法 asyncTask1 开始执行，执行线程为main
     [           main] cn.ohbee.demoasync.AsyncTask             : 同步方法 asyncTask1 结束执行，执行线程为main
     [           main] cn.ohbee.demoasync.AsyncTask             : 同步方法 syncTask2 开始执行，执行线程为main
     [           main] cn.ohbee.demoasync.AsyncTask             : 同步方法 syncTask2 结束执行，执行线程为main
     [           main] cn.ohbee.demoasync.AsyncTask             : 同步方法 syncTask3 开始执行，执行线程为main
     [           main] cn.ohbee.demoasync.AsyncTask             : 同步方法 syncTask3 结束执行，执行线程为main
     [           main] cn.ohbee.demoasync.AsyncTaskTest         : 同步任务执行完成共: 18034 毫秒
     [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
     [         task-1] cn.ohbee.demoasync.AsyncTask             : 异步方法 asyncTask1 开始执行，执行线程为task-1
     [         task-2] cn.ohbee.demoasync.AsyncTask             : 异步方法 asyncTask2 开始执行，执行线程为task-2
     [         task-3] cn.ohbee.demoasync.AsyncTask             : 异步方法 asyncTask3 开始执行，执行线程为task-3
     [         task-3] cn.ohbee.demoasync.AsyncTask             : 异步方法 asyncTask3 结束执行，执行线程为task-3
     [         task-2] cn.ohbee.demoasync.AsyncTask             : 异步方法 asyncTask2 结束执行，执行线程为task-2
     [         task-1] cn.ohbee.demoasync.AsyncTask             : 异步方法 asyncTask1 结束执行，执行线程为task-1
     [           main] cn.ohbee.demoasync.AsyncTaskTest         : this is asyncTask1, 执行完成
     [           main] cn.ohbee.demoasync.AsyncTaskTest         : this is asyncTask3, 执行完成
     [           main] cn.ohbee.demoasync.AsyncTaskTest         : this is asyncTask2, 执行完成
     [           main] cn.ohbee.demoasync.AsyncTaskTest         : 异步任务执行完成共: 10079 毫秒
     [extShutdownHook] o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'applicationTaskExecutor'
```
> 可见每个异步任务都创建了一个线程执行。
###### ⏰ 异步任务的配置属性<span id="异步任务的配置属性"></span>
- `spring.task.execution.pool.allow-core-thread-timeout`，默认值 `true`。是否允许核心线程超时。这个设置可以动态增大或者缩小线程池。
- `spring.task.execution.pool.core-size`，默认值`8.0`。核心线程数。
- `spring.task.execution.pool.keep-alive`，默认值`60s`。线程的最大空闲时间，当线程在配置时间段内空间，就会被回收。
- `spring.task.execution.pool.max-size`，`无默认`配置。允许最大线程数，如果任务填满队列，则线程池可以扩展到该大小以容纳负载。并忽略队列是否无界。
- `spring.task.execution.pool.queue-capacity`，`无默认`值。队列容量，无限容量也不会增加线程池的大小，这意味者忽略了`max-size`属性。
- `spring.task.execution.thread-name-prefix`，默认值`task-`。新增线程名称前缀。
--- 
🐟 <strong> Hi~ o(*￣▽￣*)ブ <span id="参考"> </strong><a  target="_blank" href="https://docs.spring.io/spring-boot/docs/2.4.1/reference/htmlsingle/#spring.task.execution.pool.allow-core-thread-timeout">🌱参考文档</a>
🐋 <strong> Hi~ o(*￣▽￣*)ブ <span id="源码地址"> </strong><a  target="_blank" href="https://github.com/SpanishSoap/spring-boot-ohbee">🌱源码地址</a>