# 1. Spring Boot 中使用拦截器
###### ⏰ 关键代码<span id="关键代码"></span>
---
- 🌏 实现 `HandlerInterceptor`接口，重写 `preHandle`方法实现拦截逻辑
    ```java
    public class DemoInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
             ...
            return true;
        }
    ```
- 🌏 实现 `WebMvcConfigurer`接口，重写 `addInterceptors`方法，注册拦截器
    ```java
    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
          ...
        }
    }
    ```
###### ⏰ Maven依赖<span id="Maven依赖"></span>
---
- 🌏 pom.xml 中使用如下依赖，如下：
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
           <!--web 项目依赖-->
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-web</artifactId>
           </dependency>
       </dependencies>
   ```

###### ⏰ Controller中定义web接口<span id="创建Controller"></span>
---
- 🌏 程序中，创建 `DemoController` 类，定义`simpleWebApi`接口，模拟web请求。如下：
    ```java
    @Slf4j
    @RestController
    @Validated
    public class DemoController {
        @GetMapping("simpleWebApi")
        public List<String> simpleWebApi(@NotNull Boolean isBoys){
            log.info("方法执行中:simpleWebApi");
            ArrayList<String> friends ;
            if(isBoys){
                friends =  CollUtil.newArrayList("Joey", "Chandler", "Rose");
            }else {
                friends =  CollUtil.newArrayList("Monica", "Rachel", "Phoebe");
            }
            return friends;
        }
    }
    ```

###### ⏰ 实现拦截器<span id="实现拦截器"></span>
---
-  🌏 实现 `HandlerInterceptor` 接口
    ```java
    @Slf4j
    public class DemoInterceptor implements HandlerInterceptor {
        /**
         * 在方法实际处理之前执行
         * @param request
         * @param response
         * @param handler
         * @return
         * @throws Exception
         */
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            //HandlerMethod类   提供了对方法参数，方法返回值，方法注释等的便捷访问。
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            log.info("方法执行前执行:preHandle");
            //返回true向下执行，返回false中断请求
            return true;
        }
        /**
         * 在方法处理之后执行
         * @param request
         * @param response
         * @param handler
         * @param modelAndView
         * @throws Exception
         */
        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
            log.info("方法执行后执行:postHandle");
        }
        /**
         * 请求完整完成之后执行
         * @param request
         * @param response
         * @param handler
         * @param ex
         * @throws Exception
         */
        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            log.info("方法执行完成，准备返回数据或者视图的时候执行:afterCompletion");
        }
    }
   ```
   > 我们重写了接口中的 `preHandle` 方法，当此方法返回 `true` 代表通过拦截器，`false` 代表被拦截器拒绝，请求终止不会再转发到 `controller`中。同时可以选择性的重写 `postHandle` 或者 `afterCompletion` 方法。
###### ⏰注册拦截器<span id="注册拦截器"></span>
---   
-  🌏 实现 `WebMvcConfigurer` ，重写 `addInterceptors` 方法
    ```java
    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new DemoInterceptor()).addPathPatterns("/*");
        }
    }
    ```
   > `registry.addInterceptor(new DemoInterceptor()).addPathPatterns("/*");` 添加 `DemoInterceptor` 拦截器，并添加拦截路径    `/*` 代表拦截所有 web 请求。
###### ⏰ 请求接口，查看打印<span id="测试"></span>
---   
-   🌏 浏览器中访问 `http://localhost:8080/simpleWebApi?isBoys=true`，查看打印日志
    ```sh
    方法执行前执行:preHandle
    方法执行中:simpleWebApi
    方法执行后执行:postHandle
    方法执行完成，准备返回数据或者视图的时候执行:afterCompletion
    ```
--- 
🐟 <strong> Hi~ o(*￣▽￣*)ブ <span id="参考"> </strong><a  target="_blank" href="https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-config-interceptors">🌱参考文档</a>
🐋 <strong> Hi~ o(*￣▽￣*)ブ <span id="源码地址"> </strong><a  target="_blank" href="https://github.com/SpanishSoap/spring-boot-ohbee">🌱源码地址</a>