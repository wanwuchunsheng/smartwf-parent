package com.smartwf.sso.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Table;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/19 11:13
 * @Description: 角色权限中间表
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "sys_role_permission")
@Data
public class RolePermission {


    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 权限id
     */
    private Integer permissionId;
}
