package com.smartwf.sso.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Table;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/19 11:09
 * @Description: 用户角色中间表
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "sys_user_role")
@Data
public class UserRole {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 角色id
     */
    private Integer roleId;
}
