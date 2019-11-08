package com.smartwf.sso.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartwf.common.dto.LogDTO;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Date: 2018/12/18 15:41
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
