package com.smartwf.sso.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/19 11:11
 * @Description: 权限
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "sys_permission")
@Data
public class Permission {


    /**
     * id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限代码
     */
    private String permissionCode;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 权限描述
     */
    private String description;

    /**
     * 子级权限
     */
    @Transient
    private List<Permission> children;

    /**
     * 是否选中
     */
    @Transient
    private Boolean flag = false;
}
