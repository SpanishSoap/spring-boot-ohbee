# 1. 项目结构<span id="项目结构"></span>
###### ⏰ 项目结构
- ⭕🌏 Spring Boot Demo 的项目结构如下：
![image](https://media.ohbee.cn/spring-boot-1.jpg)
###### ⏰ demo 目标
- ⭕🌏 将单配置文件拆分为多配置文件，达到如下目的 ① 将 redis，mq，db 等配置文件拆分为不同的配置文件，然后在 application 中集成配置文件。② 指定不同的开发环境。
# 2. 项目配置文件<span id="项目配置文件"></span>
###### ⏰ application-comm.yml
- ⭕🌏 假设我们在配置中，抽取出通用配置如下：
    ```yml
    server:
      port: 8888
    application:
      name: Spring Boot Demo
      version: 2.4.1
    ```
###### ⏰ application-dev.yml 和 application-prod.yml
- ⭕🌏 假设配置文件 dev 为开发环境配置，prod 为生产环境配置：
    ```yml
    # dev
    teleplay:
      info:
        website: test.ohbee.com
        enabled: false
    ```
    ```yml
    # prod
    teleplay:
      info:
        website: ohbee.com
        enabled: true
    ```
 ###### ⏰ application-tv.yml
 - ⭕🌏 另一份通用配置：
    ```yml
    teleplay:
      info:
        title: friends
        actors:
            - Joey
            - Phoebe
            - Rachel
            - Chandler
            - Ross
            - Monica
    ```
 ###### ❗ application.yml
  - ⭕🌏 可以通过如下配置将分散的配置合并到一起，并激活：
     ```yml
     #-------------使用如下集成方式，可激活配置
    #spring:
    #  profiles:
    #    active: comm,dev,tv

    #-------------或者使用如下集成方式，可激活配置
    #spring:
    #     profiles:
    #       include: comm,dev,tv

    #-------------或者使用如下分组的集成方式，可激活配置
    spring:
      profiles:
        active: prod
        group:
          dev: comm,dev,tv
          prod: comm,prod,tv
     ```
    > 使用 spring.profiles.group 配置可以分组激活
# 3. 获取配置文件中的配置项<span id="获取配置文件中的配置项"></span>
 ###### ⏰ 使用 @Value 获取配置
 - ⭕🌏  @Value 
    ```java
    @Data
    @Component
    public class ApplicationProperty {
        @Value("${application.name}")
        private String name;
        @Value("${application.version}")
        private String version;
    }
    ```

###### ⏰ 使用 @ConfigurationProperties 获取配置
 - ⭕🌏  @ConfigurationProperties
     ```java
    @ConfigurationProperties("teleplay.info")
    @Component
    @Data
    public class TeleplayInfoProperty {
        private String website;
        private Boolean enabled;
        private String title;
        private List<String> actors;
    }
     ```
# 4. 测试配置项<span id="测试配置项">
###### ⏰ 使用如下代码打印配置项
 - ⭕🌏  使用如下代码打印配置项
    ```java
    @SpringBootApplication
    @Slf4j
    public class DemoPropertiesApplication implements ApplicationRunner {

        public static void main(String[] args) {
            SpringApplication.run(DemoPropertiesApplication.class, args);
        }

        @Autowired
        private ApplicationProperty applicationProperty;
        @Autowired
        private TeleplayInfoProperty teleplayInfoProperty;
        @Override
        public void run(ApplicationArguments args) throws Exception {
            Dict set = Dict.create().set("applicationProperty", applicationProperty).set("blogInfoProperty", teleplayInfoProperty);
            log.info(new ObjectMapper().writeValueAsString(set));
        }
    }
    ```
###### ⏰ 启动查看日志
 - ⭕🌏  如下可见激活了 dev,comm,tv 配置
     ```shell
     INFO 32 --- [           main] c.o.d.DemoPropertiesApplication          : The following profiles are active: dev,comm,tv
     ```
 - ⭕🌏  如下可见打印的配置项
    ```json
     {
        "applicationProperty": {
            "name": "Spring Boot Demo",
            "version": "2.4.1"
        },
        "blogInfoProperty": {
            "website": "test.ohbee.com",
            "enabled": false,
            "title": "friends",
            "actors": ["Joey", "Phoebe", "Rachel", "Chandler", "Ross", "Monica"]
        }
    }
    ```
    
 ---
<strong> Hi~ o(*￣▽￣*)ブ <span id="源码地址"> </strong>🌱 [源码地址](https://github.com/SpanishSoap/spring-boot-ohbee)



- https://blog.csdn.net/yusimiao/article/details/97622666
- https://mp.weixin.qq.com/s?__biz=MzAxODcyNjEzNQ==&mid=2247515149&idx=2&sn=7d3805a95652ca6cd1d77873dd990e04&chksm=9bd31195aca49883f3087e9a65bf7cea64b10e212d9fcab2d9559615966fdc7ad275a74ab266&mpshare=1&scene=1&srcid=12229nC0K774KKcnoL1Im6t8&sharer_sharetime=1608599209617&sharer_shareid=763aa9bebb0deae3c0dd96b8c065f71b&key=da08e111de57c4e1981418c3b9829ef23af6bc2f6f2ffa56c4a8492e6ab0eed1d362c22226c1a43c85374611acf8569a7d3a88ced7ec84d0560eda92f683f21ba310e28e281f0bd20ff119f1c459d0685f61591e4cc27ca7a223faeecaf9e95cc508e659359a2ca11255a9480e8702a65aca9852e20e9294467773770ba543bc&ascene=1&uin=NjExOTkxMTgw&devicetype=Windows+10+x64&version=6300002f&lang=zh_CN&exportkey=ATPWoEYz6anbPZLf%2BPMrTI4%3D&pass_ticket=sBciIWXsKmgf5xR%2BYPcvucJDvWfdeHdz%2FaBrTqHHECbO8r%2FEafCIWHK4JcYhHo24&wx_header=0