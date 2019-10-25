package com.smartwf.zuul.config.filter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * 网关限流
 * 
 * */
@Component
public class OrderRateLimiterFilter extends ZuulFilter {

	
    //每秒产生500个令牌（请求进来后会拿去一个令牌，如果没拿到就不让访问。）
    private static final RateLimiter RATE_LIMITER = RateLimiter.create(1000);
    @Override
    public String filterType() {
        return "PRE_TYPE";
    }

    @Override
    public int filterOrder() {
        return -4;
    }

    @Override
    public boolean shouldFilter() {
        //zool 使用时，上下文对象RequestContext，是共享的。 所以通过RequestContext 获取值
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        //设置需要拦截的路径
        if ("/apigateway/order/api/v1/order/save".equalsIgnoreCase(request.getRequestURI())) {
            //拦住了
            return true;
        }
        //放行
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        //zool 使用时，上下文对象RequestContext，是共享的。 所以通过RequestContext 获取值
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            token = request.getParameter("token");
        }
        //限流  如果qps 访问过高（超过上面定义的，就停止访问）
        if (!RATE_LIMITER.tryAcquire()||StringUtils.isBlank(token)) {
            //如果token 等于null   返回状态码和没有权限的信息给前台
                requestContext.setSendZuulResponse(false);
                requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        //（JWT）解析token  获取权限列表 鉴权。
        return null;
    }

}
