package com.smartwf.common.pojo;

import java.io.Serializable;

import com.smartwf.common.constant.Constants;

import lombok.Data;

/**
 * @Date: 2018/12/19 13:26
 * @Description: 返回结果类
 */
@Data
public class AppResult<T> implements Serializable {

    public static final long serialVersionUID = 42L;


    /**
     * 状态码
     */
    private int code;


    /**
     * 消息
     */
    private String msg;


    /**
     * 数据
     */
    private T data;


    /**
     * 总数
     */
    private Long total;


    public AppResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public AppResult(int code, T data) {
        this.code = code;
        this.data = data;
    }


    public AppResult(int code, Long total, T data) {
        this.code = code;
        this.total = total;
        this.data = data;
    }


    /**
     * 返回成功信息
     * @return
     */
    public static AppResult<String> successMsg() {
        return new AppResult(Constants.ONE, Constants.SUCCESS);
    }


    /**
     * 返回错误信息
     * @return
     */
    public static AppResult<String> failMsg() {
        return new AppResult(Constants.ZERO, Constants.FAIL);
    }



    /**
     * 返回自定义信息
     * @param msg
     * @return
     */
    public static AppResult<String> msg(String msg) {
        return new AppResult(Constants.ZERO, msg);
    }


    /**
     * 返回自定义数据
     * @param data
     * @return
     */
    public static<T> AppResult<T> data(T data) {
        return new AppResult(Constants.ONE, data);
    }


    /**
     * 返回查询总数量跟自定义数据
     * @param data
     * @return
     */
    public static<T> AppResult<T> data(Long total, T data) {
        return new AppResult(Constants.ONE, total, data);
    }
}
