package com.smartwf.common.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @Auther: 
 * @Description: 基础javaBean
 */
@Data
public class BasePojo {

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateUser;

}
