package cn.ohbee.demoexception;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zf
 *
 * @RestControllerAdvice 中包含了 @ControllerAdvice 和 @ResponseBody
 *
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
