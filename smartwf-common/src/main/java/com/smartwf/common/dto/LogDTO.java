package com.smartwf.common.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Date: 2018/12/18 15:41
 * @Description: 日志DTO
 */
@Data
public class LogDTO {


    /**
     * 访问url
     */
    private String logUrl;

    /**
     * 访问用户
     */
    private String logUser;


    /**
     * 操作内容
     */
    private String logContent;


    /**
     * 操作json
     */
    private String logJson;


    /**
     * 操作时间
     */
    private Date oprationTime;


    /**
     * IP地址
     */
    private String ipAddress;


    /**
     * 结果
     */
    private Integer result;
}
