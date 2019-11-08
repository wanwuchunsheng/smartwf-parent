package com.smartwf.common.utils;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;

import com.alibaba.fastjson.JSON;
import com.smartwf.common.annotation.RequiresPermissions;
import com.smartwf.common.constant.Constants;
import com.smartwf.common.exception.CommonException;
import com.smartwf.common.pojo.Logical;
import com.smartwf.common.pojo.User;
import com.smartwf.common.service.RedisService;
import com.smartwf.common.thread.PermissionThreadLocal;
import com.smartwf.common.thread.UserThreadLocal;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: 赵明明
 * @Date: 2018/11/5 15:09
 * @Description: 登录工具类
 */
@Slf4j
public class LoginUtils {

    public static boolean checkLogin(HttpServletRequest request, HttpServletResponse response, Object handler, RedisService redisService) {
        // 判断是否登录
        String token = request.getHeader(Constants.SMARTWF_TOKEN);
        if (StringUtils.isBlank(token)) {
            throw new CommonException(Constants.UNAUTHORIZED, "请先登录！");
        }
        Set<String> userSet = redisService.hget(token);
        if (userSet.isEmpty()) {
            log.warn("token失效：{}，用户请求uri：{}", token, request.getRequestURI());
            throw new CommonException(Constants.UNAUTHORIZED, "用户已失效！请重新登录！");
        } else {
            String permission = new String();
            // 重新设置redis时间
            redisService.expire(token, 14400);
            for (String userJson : userSet) {
                User user = JSON.parseObject(userJson, User.class);
                permission = redisService.hget(token, userJson);
                // 用户信息、权限信息放入本地线程
                PermissionThreadLocal.setPermission(permission);
                UserThreadLocal.setUser(user);
                break;
            }

            // 获取方法
            if(handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod)handler;
                RequiresPermissions annotation = handlerMethod.getMethodAnnotation(RequiresPermissions.class);
                if (StringUtils.isBlank(permission)) {
                    throw new CommonException(Constants.FORBIDDEN, "暂无权限！");
                }
                // 判断是否有权限注解
                if (annotation != null) {
                    // 校验
                    String[] value = annotation.value();
                    Logical logical = annotation.logical();
                    if (logical.equals(Logical.AND)) {
                        // 是AND
                        for (String per : value) {
                            /**
                             * 注解权限均为三级权限
                             */
                            if (!permission.contains(per)) {
                                // 无权限
                                throw new CommonException(Constants.FORBIDDEN, "暂无权限！");
                            }
                        }
                        return true;
                    } else if (logical.equals(Logical.OR)) {
                        int count = 0;
                        // 是OR
                        for (String per : value) {
                            if (!permission.contains(per)) {
                                // 无权限
                                count++;
                            }
                        }
                        if (count == value.length) {
                            throw new CommonException(Constants.FORBIDDEN, "暂无权限！");
                        }
                    }
                }
            }
            return true;
        }
    }
}
