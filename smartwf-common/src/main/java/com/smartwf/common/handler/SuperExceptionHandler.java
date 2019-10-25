package com.smartwf.common.handler;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smartwf.common.pojo.Result;

import lombok.extern.slf4j.Slf4j;

/**
 * 统一异常处理
 */
@ControllerAdvice
@Slf4j
public class SuperExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<Result> handlerException(Exception eception){
        if (null != eception.getStackTrace()) {
            StackTraceElement[] stackTrace = eception.getStackTrace();
            log.error("捕捉到异常具体信息：{}", stackTrace);
        }
        log.error("捕捉到异常：{}，信息：{}", eception, eception.getMessage());
        if (eception instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException e = (MethodArgumentNotValidException) eception;
            BindingResult bindingResult = e.getBindingResult();
            return this.binding(bindingResult);
        } else if (eception instanceof BindException) {
            BindException e = (BindException) eception;
            BindingResult bindingResult = e.getBindingResult();
            return this.binding(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.failMsg());
    }


    /**
     * 获取错误信息
     * @param bindingResult
     * @return
     */
    private ResponseEntity<Result> binding(BindingResult bindingResult) {
        String retVal = null;
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError allError : allErrors) {
                retVal = allError.getDefaultMessage();
                break;
            }
        }
        return ResponseEntity.badRequest().body(Result.msg(retVal));
    }
}