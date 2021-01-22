# 1. Spring Boot 中异常处理
###### ⏰ 关键代码<span id="关键代码"></span>
---
- 🌏 使用 `@RestControllerAdvice` 拦截所有 `controller` 异常，使用 `@ExceptionHandler` 标记需要处理的异常
    ```java
    @RestControllerAdvice
    public class SimpleExceptionHandler {
        @ExceptionHandler(RuntimeException.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public void simpleExceptionHandlerMethod(RuntimeException e){
           ...
        };
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
- 🌏 程序中，创建 `SimpleExceptionController` 类，定义 `simpleWebApi` 和 `nullWebApi` 接口，模拟web请求。如下：
    ```java
    @Slf4j
    @RestController
    public class SimpleExceptionController {
        @GetMapping("simpleWebApi")
        public void simpleWebApi(){
            throw new RuntimeException("运行时异常");
        }
        @GetMapping("nullWebApi")
        public void nullWebApi()  {
            throw new NullPointerException("空指针异常");
        }
    }
    ```

###### ⏰ 定义异常处理类<span id="定义异常处理类"></span>
---
-  🌏 定义 `SimpleExceptionHandler` 异常处理类
    ```java
    /**
     * @RestControllerAdvice 是 @ControllerAdvice 和 @ResponseBody 的集合
     * 默认扫描 basePackages 下的类
     */
    @RestControllerAdvice
    public class SimpleExceptionHandler {
        /**
         *  异常类范例
         * @param e 抛出的异常类
         * @return
         */
        @ExceptionHandler(RuntimeException.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public String simpleExceptionHandlerMethod(RuntimeException e){
            return e.getMessage();
        };
        /**
         * 常见请求参数
         * 更多见: https://docs.spring.io/spring-framework/docs/5.3.2/reference/html/web.html#mvc-ann-exceptionhandler-args
         * @param request  http请求报文的request
         * @param response http请求报文的response
         * @param handlerMethod  引发异常的控制器方法
         * @param e 抛出的具体异常
         * @return
         */
        @ExceptionHandler(NullPointerException.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public Map<String,String> argumentsAndReturnValueExceptionHandlerMethod(ServletRequest request, ServletResponse response, HttpMethod httpMethod, HandlerMethod handlerMethod, NullPointerException e){
            Map<String,String> rMap = new HashMap<>();
            rMap.put("错误信息",e.getMessage());
            return rMap;
        };
    }
    ```
   > ` @RestControllerAdvice`  中包含了  `@ControllerAdvice`  和 `@ResponseBody` ，支持直接返回 `JSON` 数组。`@ExceptionHandler` 指定处理的异常类，`@ResponseStatus` 指定 `response`码。
   > `@ExceptionHandler` 所在的方法，对参数有限制，常用的有 `ServletRequest`(http请求报文的request)，`ServletResponse`(http请求报文的response)，`HandlerMethod`(引发异常的控制器方法)，`Exception`(接收的异常类)。
   > 最常用的返回值为 `Map`类

###### ⏰ 请求接口，查看打印<span id="测试"></span>
---   
-   🌏 浏览器中访问 `localhost:8080/simpleWebApi`，查看返回
    ```
    运行时异常
    ```
-   🌏 浏览器中访问 `localhost:8080/nullWebApi`，查看返回
      ```json
       {
           "错误信息": "空指针异常"
       }
      ```
###### ⏰ 拓展<span id="拓展"></span>
- 🌏 可以在 `@RestControllerAdvice` 注解中指定包路径，类，或者Controller处理器注解，来缩小捕捉异常的范围。

--- 
🐟 <strong> Hi~ o(*￣▽￣*)ブ <span id="参考"> </strong><a  target="_blank" href="https://docs.spring.io/spring-framework/docs/5.3.2/reference/html/web.html#mvc-ann-exceptionhandler-args">🌱参考文档</a>
🐋 <strong> Hi~ o(*￣▽￣*)ブ <span id="源码地址"> </strong><a  target="_blank" href="https://github.com/SpanishSoap/spring-boot-ohbee">🌱源码地址</a>