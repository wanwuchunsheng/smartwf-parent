package com.smartwf.sso.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/19 11:13
 * @Description: 角色权限中间表DTO
 */
@Data
@ApiModel
public class RolePermissionDTO {


    /**
     * 角色id
     */
    @ApiModelProperty(name = "roleId", value = "角色id")
    private Integer roleId;

    /**
     * 权限id数组
     */
    @ApiModelProperty(name = "permissionIds", value = "权限id数组")
    private List<Integer> permissionIds;

}
