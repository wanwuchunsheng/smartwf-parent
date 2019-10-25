package com.smartwf.hm.sys.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartwf.common.dto.LogDTO;

import lombok.Data;

/**
 * @Date: 2019-10-25 15:35:18
 * @Description: 日志
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Data
@Table(name = "sys_log")
public class Log extends LogDTO {


    /**
     * id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

}
