package com.smartwf.common.pojo;

import lombok.Data;

/**
 * @Auther: WCH
 * @Date: 2018/9/20 14:52
 * @Description: 分页
 */
@Data
public class Page {

    /**
     * 第几页
     */
    private Integer page = 1;

    /**
     * 每页查询数量
     */
    private Integer limit = 10;
}
