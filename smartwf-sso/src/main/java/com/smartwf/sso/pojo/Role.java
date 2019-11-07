package com.smartwf.sso.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/19 11:07
 * @Description: 角色
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "sys_role")
@Data
public class Role {

    /**
     * id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 角色名
     */
    @NotEmpty(message = "角色名称不能为空！")
    private String roleName;

    /**
     * 所属企业id   超级管理员创建的角色企业为-1  平台管理员创建的角色企业为-2
     */
    private Integer companyId;

    /**
     * 是否为系统角色  1：是  0：否
     */
    private Integer systemRole;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 是否有效  1：有效  0：无效。默认有效
     */
    private Integer enable;


    /**
     * 门店id
     */
    private Integer shopId;
}
