package cn.ohbee.demoaop;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.omg.CORBA.UNKNOWN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zf
 */

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
