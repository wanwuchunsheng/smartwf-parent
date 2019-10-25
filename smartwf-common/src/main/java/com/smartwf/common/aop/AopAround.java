package com.smartwf.common.aop;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.smartwf.common.annotation.TraceLog;
import com.smartwf.common.pojo.Result;
import com.smartwf.common.queue.LogQueue;

/**
 * @Auther: 
 * @Description: AOP切面
 */
public class AopAround {


    /**
     * 完成统一校验pojo参数
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    public static Object bindingResultAround(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        BindingResult bindingResult = null;
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                bindingResult = (BindingResult) arg;
                break;
            }
        }
        if (bindingResult != null) {
            String retVal = null;
            if (bindingResult.hasErrors()) {
                List<ObjectError> allErrors = bindingResult.getAllErrors();
                for (ObjectError allError : allErrors) {
                    retVal = allError.getDefaultMessage();
                    break;
                }
                return ResponseEntity.badRequest().body(Result.msg(retVal));
            }
        }
        return pjp.proceed();
    }


    /**
     * 日志AOP
     * @param joinPoint
     * @param retValue
     */
    public static void setLogQueue(JoinPoint joinPoint, Object retValue ) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        TraceLog traceLog = method.getAnnotation(TraceLog.class);
        String content = traceLog.content();
        String argsJson = null;
        int[] paramIndexs = traceLog.paramIndexs();
        if (paramIndexs.length != 0) {
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < paramIndexs.length; i++) {
                Object arg = args[paramIndexs[i]];
                if (argsJson == null) {
                    argsJson = JSON.toJSONString(arg);
                } else {
                    argsJson += ";" + JSON.toJSONString(arg);
                }

            }
        }
        int statusCodeValue = 200;
        if (retValue instanceof ResponseEntity) {
            statusCodeValue = ((ResponseEntity) retValue).getStatusCodeValue();
        }
        LogQueue.setQueue(request, content, JSON.toJSONString(argsJson), statusCodeValue);
    }
}
