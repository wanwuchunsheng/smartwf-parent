package com.smartwf.sso.dto;

import lombok.Data;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/19 11:15
 * @Description: 用户角色DTO
 */
@Data
public class UserRoleDTO {


    /**
     * 用户id
     */
    private Integer userId;


    /**
     * 角色id，多个以英文逗号分隔
     */
    private String roleIds;

}
