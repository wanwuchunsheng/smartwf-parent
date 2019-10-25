package com.smartwf.common.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smartwf.common.exception.CommonException;
import com.smartwf.common.pojo.AppResult;

import lombok.extern.slf4j.Slf4j;

/**
 * 统一异常处理
 */
@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    @ExceptionHandler(value = CommonException.class)
    @ResponseBody
    public ResponseEntity<AppResult> handlerException(CommonException e) {
        if (null != e.getStackTrace()) {
            StackTraceElement[] stackTrace = e.getStackTrace();
            log.error("捕捉到异常具体信息：{}", stackTrace);
        }
        log.error("捕捉到异常：{}", e, e.getMsg());
        return ResponseEntity.status(e.getCode()).body(AppResult.msg(e.getMsg()));
    }

}