package cn.exrick.xboot.config.interceptor;

import cn.exrick.xboot.common.annotation.RateLimiter;
import cn.exrick.xboot.common.constant.CommonConstant;
import cn.exrick.xboot.common.exception.LimitException;
import cn.exrick.xboot.common.limit.RedisRaterLimiter;
import cn.exrick.xboot.common.utils.IpInfoUtil;
import cn.exrick.xboot.config.properties.XbootIpLimitProperties;
import cn.exrick.xboot.config.properties.XbootLimitProperties;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 限流拦截器
 * 
 */
@Slf4j
@Component
public class LimitRaterInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private XbootLimitProperties limitProperties;

    @Autowired
    private XbootIpLimitProperties ipLimitProperties;

    @Autowired
    private RedisRaterLimiter redisRaterLimiter;

    @Autowired
    private IpInfoUtil ipInfoUtil;

    /**
     * 预处理回调方法，实现处理器的预处理（如登录检查）
     * 第三个参数为响应的处理器，即controller
     * 返回true，表示继续流程，调用下一个拦截器或者处理器
     * 返回false，表示流程中断，通过response产生响应
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        String ip = ipInfoUtil.getIpAddr(request);

        if (ipLimitProperties.getEnable()) {
            Boolean token1 = redisRaterLimiter.acquireByRedis(ip,
                    ipLimitProperties.getLimit(), ipLimitProperties.getTimeout());
            if (!token1) {
                throw new LimitException("你手速怎么这么快，请点慢一点");
            }
        }

        if (limitProperties.getEnable()) {
            Boolean token2 = redisRaterLimiter.acquireByRedis(CommonConstant.LIMIT_ALL,
                    limitProperties.getLimit(), limitProperties.getTimeout());
            if (!token2) {
                throw new LimitException("当前访问总人数太多啦，请稍后再试");
            }
        }

        try {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Object bean = handlerMethod.getBean();
            Method method = handlerMethod.getMethod();
            RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);
            if (rateLimiter != null) {
                String name = rateLimiter.name();
                Long limit = rateLimiter.rate();
                Long timeout = rateLimiter.rateInterval();
                if(StrUtil.isBlank(name)){
                    name = StrUtil.subBefore(bean.toString(), "@", false) + "_" + method.getName();
                }
                if (rateLimiter.ipLimit()) {
                    name += "_" + ip;
                }
                Boolean token3 = redisRaterLimiter.acquireByRedis(name, limit, timeout);
                if (!token3) {
                    throw new LimitException("当前访问人数太多啦，请稍后再试");
                }
            }
        } catch (LimitException e) {
            throw new LimitException(e.getMsg());
        } catch (Exception e) {

        }

        return true;
    }

    /**
     * 当前请求进行处理之后，也就是Controller方法调用之后执行，
     * 但是它会在DispatcherServlet 进行视图返回渲染之前被调用。
     * 此时我们可以通过modelAndView对模型数据进行处理或对视图进行处理。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 方法将在整个请求结束之后，也就是在DispatcherServlet渲染了对应的视图之后执行。
     * 这个方法的主要作用是用于进行资源清理工作的。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
    }
}
