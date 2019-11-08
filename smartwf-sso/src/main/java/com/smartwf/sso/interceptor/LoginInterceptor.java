package com.smartwf.sso.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.smartwf.common.thread.PermissionThreadLocal;
import com.smartwf.common.thread.UserThreadLocal;

import com.smartwf.common.service.RedisService;
import com.smartwf.common.utils.LoginUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 登录拦截器
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {


    @Autowired
    private RedisService redisService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        return LoginUtils.checkLogin(request, response, handler, redisService);
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 当请求完成之后，消除用户信息、权限信息
        PermissionThreadLocal.setPermission(null);
        UserThreadLocal.setUser(null);
    }

}