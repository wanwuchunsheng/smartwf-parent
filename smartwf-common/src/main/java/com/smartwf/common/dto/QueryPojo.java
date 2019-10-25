package com.smartwf.common.dto;

import java.util.Date;

import lombok.Data;

/**
 * @Date: 2018/11/20 14:33
 * @Description: 查询DTO
 */
@Data
public class QueryPojo {

    /**
     * 企业id
     */
    private Integer companyId;

    /**
     * 门店id
     */
    private Integer shopId;

    /**
     * 单号
     */
    private String orderNo;

    /**
     * 名称
     */
    private String name;

    /**
     * 是否有效，1：有效0：无效。
     */
    private String enable;

    /**
     * 摊位类型id
     */
    private Integer boothTypeId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;
}
