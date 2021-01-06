# 1. 创建可部署war包
###### ⏰ 第一步
---
<div style="background:#f0f3e8;padding:10px">
❗  由于 Spring WebFlux 不严格依赖 Servlet API，并且默认情况下应用程序部署在嵌入式 Reactor Netty 服务器上，因此 WebFlux 应用程序不支持 War 部署。
</div>

- ⭕🌏 若要生成可部署 `war` 文件，第一步需要提供一个 `SpringBootServletInitializer ` 子类并覆写 `configure` 方法。如此配置，使用了 Spring Framework 的 Servlet 3.0 支持，并且可以将 war 包部署在外部的 servlett 容器中，通常来说，我们需要修改应用的启动类，继承 `SpringBootServletInitializer` ，如下面范例所示：
    ```java
    @SpringBootApplication
    public class Application extends SpringBootServletInitializer {
        @Override
        protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
            return application.sources(Application.class);
        }
        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }

    }
    ```

###### ⏰ 第二步
 ---
 - ⭕🌏 下一步是更新构建配置，以便我们的项目生成 war 文件而不是 jar 文件。如果我们使用的是 `Maven ` 和 `spring-boot-starter-parent`( 其默认为我们配置了 Maven 的 war 插件)，我们需要做的是在 `pom.xml` 文件中修改打包方式为 `war`，如下：
    ```xml
    <packaging>war</packaging>
    ```
---
###### ⏰ 最后一步
- ⭕🌏 最后一步是确保嵌入式 `servlet` 容器不干扰 `war` 文件所部署到的外部 `servlet` 容器。在 `pom.xml` 文件中将内置的 `tomcat` 容器标记为已提供。
- 如下范例所示：
    ```xml
    <dependencies>
        <!-- … -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
            <scope>provided</scope>
        </dependency>
        <!-- … -->
    </dependencies>
    ```
---    
<div style="background:#eef5dc;padding:10px">
🔵  如果我们使用的是 Spring Boot 自带的构建工具(Spring Boot Maven Plugin)，打war 包生成的是可执行的 war 包，打包过程中将需要的依赖打包到了 lib-provided 文件夹下。这意味者，此 war 包除了可以部署在 servlet 容器中，我们也可以在命令行中使用 java -jar 来启动应用。
</div>

--- 
🐟 <strong> Hi~ o(*￣▽￣*)ブ <span id="参考"> </strong>🌱 [参考](https://docs.spring.io/spring-boot/docs/2.4.1/reference/htmlsingle/#howto-create-a-deployable-war-file)
🐋 <strong> Hi~ o(*￣▽￣*)ブ <span id="源码地址"> </strong>🌱 [源码地址](https://github.com/SpanishSoap/spring-boot-ohbee)