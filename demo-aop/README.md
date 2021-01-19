# 1. Spring Boot中的AOP
###### ⏰ 关键代码<span id="关键代码"></span>
---
- 🌏 使用 `@Aspect` 和`@Component`注解标记类为切面，并使用`@Pointcut`定义切入点，使用`@Around`定义通知
    ```java
    @Aspect
    @Component
    @Slf4j
    public class AopTestSimple {
        /**
         * 定义切入点
         */
        @Pointcut("execution(public * cn.ohbee.demoaop.*Controller.*(..))")
        public void aopTestSimplePointcut() {}

        /**
         *  定义环绕通知，①解析浏览器请求信息，②获取调取方法的信息，③获取方法执行完的返回结果
         * @param point  通知的第一个固有参数
         */
        @Around("aopTestSimplePointcut()")
        public Object aopTestSimpleAround(ProceedingJoinPoint point) throws Throwable {
            //目标方法执行
            Object  proceed = point.proceed();
            ...
            return proceed;
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
           <!--AOP 项目依赖-->
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-aop</artifactId>
           </dependency>
       </dependencies>
   ```
  >  项目中添加了 web 开发依赖 `spring-boot-starter-web` 和 AOP 依赖 `spring-boot-starter-aop`。
---
###### ⏰ 创建Controller的web接口<span id=" 创建Controller"></span>
---
- 🌏 程序中我们创建 `AopTestSimpleController` 类，定义`simpleWebApi`接口，模拟web请求。如下：
    ```java
    @Slf4j
    @RestController
    public class AopTestSimpleController {
        @GetMapping("simpleWebApi")
        public List<String> aopTestSimpleWebApi(@NotNull Boolean isBoys){
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

###### ⏰ 定义AOP<span id="定义AOP"></span>
---
-  AOP 中定义环绕通知，并打印日志
    ```java
    @Aspect
    @Component
    @Slf4j
    public class AopTestSimple {
        private final ObjectMapper objectMapper;
        public AopTestSimple(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }
        /**
         * 定义切入点
         */
        @Pointcut("execution(public * cn.ohbee.demoaop.*Controller.*(..))")
        public void aopTestSimplePointcut() {}
        /**
         *  定义环绕通知，①解析浏览器请求信息，②获取调取方法的信息，③获取方法执行完的返回结果
         * @param point  通知的第一个固有参数
         */
        @Around("aopTestSimplePointcut()")
        public Object aopTestSimpleAround(ProceedingJoinPoint point) throws Throwable {
            //获取http请求
            HttpServletRequest httpServletRequest = getHttpServletRequest();
            //开启计时器
            Stopwatch started = Stopwatch.createStarted();
            //目标方法执行
            Object  proceed = point.proceed();
            //获取浏览器信息
            String userAgentHeader = httpServletRequest.getHeader("User-Agent");
            UserAgent parseUserAgent = UserAgentUtil.parse(userAgentHeader);
            //获取请求信息，ip，方法信息，参数
            String ipAddr = getIpAddr(httpServletRequest);
            String method = httpServletRequest.getMethod();
            String signature = String.format("%s.%s", point.getSignature().getDeclaringTypeName(),
                    point.getSignature().getName());
            Map<String, Object> nameValue = getNameValue(point);
            String url = httpServletRequest.getRequestURL().toString();
            //结束计时器
            long elapsed = started.stop().elapsed(TimeUnit.MILLISECONDS);
            log.info("\n请求时间:{},\n花费时间:{},\n浏览器信息:{},\n请求ip:{},\n请求url:{},\n请求类型:{},\n请求路径:{},\n请求参数:{},\n返回参数:{}",
                    DateUtil.formatDateTime(new Date()),
                    elapsed,
                    objectMapper.writeValueAsString(parseUserAgent),
                    ipAddr,
                    url,
                    method,
                    signature,
                    objectMapper.writeValueAsString(nameValue),
                    objectMapper.writeValueAsString(proceed));
            return proceed;
        }
        private Map<String, Object> getNameValue(ProceedingJoinPoint point) {
            final Signature signature = point.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            final String[] names = methodSignature.getParameterNames();
            final Object[] args = point.getArgs();
            if (ArrayUtil.isEmpty(names) || ArrayUtil.isEmpty(args)) {
                return Collections.emptyMap();
            }
            if (names.length != args.length) {
                log.warn("{}方法参数名和参数值数量不一致!", methodSignature.getName());
                return Collections.emptyMap();
            }
            Map<String, Object> map = Maps.newHashMap();
            for (int i = 0; i < names.length; i++) {
                map.put(names[i], args[i]);
            }
            return map;
        }
        /**
         * 获取 ip
         * @param request
         * @return
         */
        public static String getIpAddr(HttpServletRequest request) {
            String ipAddress ;
            final String UNKNOWN = "unknown";
            try {
                ipAddress = request.getHeader("x-forwarded-for");
                if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                    ipAddress = request.getHeader("Proxy-Client-IP");
                }
                if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                    ipAddress = request.getHeader("WL-Proxy-Client-IP");
                }
                if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                    ipAddress = request.getRemoteAddr();
                    String localhost = "127.0.0.1";
                    if (localhost.equals(ipAddress)) {
                        // 根据网卡取本机配置的IP
                        InetAddress inet = null;
                        try {
                            inet = InetAddress.getLocalHost();
                        } catch (UnknownHostException e) {
                            log.error(e.getMessage(), e);
                        }
                        ipAddress = inet.getHostAddress();
                    }
                }
                // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
                // "***.***.***.***".length()= 15
                if (ipAddress != null && ipAddress.length() > 15) {
                    if (ipAddress.indexOf(",") > 0) {
                        ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                    }
                }
            } catch (Exception e) {
                ipAddress="";
            }
            return ipAddress;
        }
        private HttpServletRequest getHttpServletRequest() {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
        }
    }
    ```
   > 定义`AopTestSimple`使用`@Aspect`将类标记为切面，使用` @Component`交由spring管理bean。类中使用`@Pointcut`定义切入点，` @Around`注解引用切入点方法，执行环绕通知的逻辑，`ProceedingJoinPoint`是通知方法的第一个固有参数，其存储了被代理对象的所有信息。
###### ⏰ 请求接口，查看打印<span id="测试"></span>
---   
- 浏览器中访问 `http://localhost:8080/simpleWebApi?isBoys=true`，查看打印日志
    ```sh
    请求时间:2020-01-19 18:02:16,
   花费时间:4,
   浏览器信息:{"mobile":false,"browser":{"name":"Chrome","pattern":"chrome","mobile":false,"unknown":false},"platform":{"name":"Windows","pattern":"windows","mobile":false,"ipad":false,"iphoneOrIPod":false,"ios":false,"android":false,"unknown":false},"os":{"name":"Windows 10 or Windows Server 2016","pattern":"windows nt 10\\.0","unknown":false},"engine":{"name":"Webkit","pattern":"webkit","unknown":false},"version":"87.0.4280.88","engineVersion":"537.36"},
   请求ip:0:0:0:0:0:0:0:1,
   请求url:http://localhost:8080/simpleWebApi,
   请求类型:GET,
   请求路径:cn.ohbee.demoaop.AopTestSimpleController.aopTestSimpleWebApi,
   请求参数:{"isBoys":true},
   返回参数:["Joey","Chandler","Rose"]
    ```
--- 
🐟 <strong> Hi~ o(*￣▽￣*)ブ <span id="参考"> </strong><a  target="_blank" href="https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop-at-aspectj">🌱参考文档</a>
🐋 <strong> Hi~ o(*￣▽￣*)ブ <span id="源码地址"> </strong><a  target="_blank" href="https://github.com/SpanishSoap/spring-boot-ohbee">🌱源码地址</a>