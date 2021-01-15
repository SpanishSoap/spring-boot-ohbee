# 1. 执行定时任务
###### ⏰ 关键代码<span id="关键代码"></span>
---
- ⭕🌏 使用 `@EnableScheduling` 注解开启SpringBoot定时任务支持
    ```java
    @SpringBootApplication
    @EnableScheduling
    public class DemoSchedulingApplication {
        public static void main(String[] args) {
            SpringApplication.run(DemoSchedulingApplication.class, args);
        }
    }
    ```
- ⭕🌏 使用 `@Scheduled` 注解标识定时任务，并给出执行周期
    ```java
    @Scheduled(cron = "0/5 * * * * ?")
    public void  taskCorn(){
        log.info("taskCorn:执行时间{}", DateUtil.formatDateTime(new Date()));
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
               <groupId>cn.hutool</groupId>
               <artifactId>hutool-all</artifactId>
           </dependency>
           <dependency>
               <groupId>org.projectlombok</groupId>
               <artifactId>lombok</artifactId>
               <optional>true</optional>
           </dependency>
       </dependencies>
   ```
  >  `spring-boot-starter` 引入的包，其中包含了定时任务的功能，其余的都是工具包。
---
###### ⏰ 主体代码<span id="测试主体代码"></span>
- ⭕🌏 程序中我们创建 `SchedulingTask` 类，模拟执行定时任务。如下：
    ```java
    @Component
    @Slf4j
    public class SchedulingTask {

        /**
         * 使用 cron 表达式
         */
        //@Scheduled(cron = "0/5 * * * * ?")
        public void  taskCorn(){
            log.info("taskCorn:执行时间{}", DateUtil.formatDateTime(new Date()));
        }

        /**
         * 上次执行完成后，经过 fixedDelay 间隔后，再执行下次任务，毫秒，long类型
         */
        //@Scheduled(fixedDelay = 2000)
        public void  fixedDelay() throws InterruptedException {
            log.info("fixedDelay:开始执行{}", DateUtil.formatDateTime(new Date()));
            TimeUnit.SECONDS.sleep(3L);
            log.info("fixedDelay:执行结束{}", DateUtil.formatDateTime(new Date()));
        }

        /**
         * 上次执行完成后，经过 fixedDelay 间隔后，再执行下次任务，毫秒，字符串
         */
        //@Scheduled(fixedDelayString = "5000")
        public void  fixedDelayString(){
            log.info("fixedDelayString:执行时间{}", DateUtil.formatDateTime(new Date()));
        }

        /**
         * 严格按间隔执行任务，（若上次任务完成，计算时间间隔开始，若上次任务未完成，则直接开始队列中的任务），毫秒，long类型
         */
        private static Integer number=0;
        //@Scheduled(fixedRate = 2000)
        public void  fixedRate() throws InterruptedException {
            number++;
            log.info("fixedRate: 第{}次开始执行{}",number, DateUtil.formatDateTime(new Date()));
            TimeUnit.SECONDS.sleep(1L);
            log.info("fixedRate: 第{}次执行结束{}", number, DateUtil.formatDateTime(new Date()));
        }

        /**
         * 严格按间隔执行任务，（若上次任务完成，计算时间间隔开始，若上次任务未完成，则直接开始队列中的任务），毫秒，字符串
         */
        //@Scheduled(fixedRateString = "5000")
        public void  fixedRateString(){
            log.info("fixedRateString:执行时间{}", DateUtil.formatDateTime(new Date()));
        }

        /**
         * 首次执行定时任务延迟时间，毫秒，long类型，配合 fixedRate() 或者 fixedDelay() 使用.
         */
        @Scheduled(initialDelay = 5000,fixedRate=2000)
        public void  initialDelay(){
            log.info("initialDelay:执行时间{}", DateUtil.formatDateTime(new Date()));
        }

        /**
         * 首次执行定时任务延迟时间，毫秒，字符串，配合 fixedRate() 或者 fixedDelay() 使用.
         */
        //@Scheduled(initialDelayString = "1000",fixedRate=2000)
        public void  initialDelayString(){
            log.info("initialDelayString:执行时间{}", DateUtil.formatDateTime(new Date()));
        }

        //zone  时区，默认为空字符串，跟随系统时区

    }
    ```
---    
###### ⏰ 定时任务的配置属性<span id="定时任务的配置属性"></span>
- `application.yml`中配置定时任务
    ```yml
    spring:
      task:
        scheduling:
          pool:
            size: 2 # 允许的最大线程数 默认为 1
          thread-name-prefix: scheduling- #新创建线程名称的前缀 默认 scheduling-
    ```
--- 
🐟 <strong> Hi~ o(*￣▽￣*)ブ <span id="参考"> </strong><a  target="_blank" href="https://docs.spring.io/spring-boot/docs/2.1.0.RELEASE/reference/htmlsingle/#boot-features-task-execution-scheduling">🌱参考文档</a>
🐋 <strong> Hi~ o(*￣▽￣*)ブ <span id="源码地址"> </strong><a  target="_blank" href="https://github.com/SpanishSoap/spring-boot-ohbee">🌱源码地址</a>