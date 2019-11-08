package com.smartwf.sso.aop;

import com.smartwf.common.aop.AopAround;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * 校验pojo参数AOP
 */
@Aspect
@Component
@Order(2)
public class BindingResultAspect {


    /**
     * 完成统一校验pojo参数
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.smartwf.sso.controller.*.*(..))")
    public Object bindingResultAround(ProceedingJoinPoint pjp) throws Throwable {
        return AopAround.bindingResultAround(pjp);
    }

}