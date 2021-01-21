package cn.ohbee.demointerceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zf
 */

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
