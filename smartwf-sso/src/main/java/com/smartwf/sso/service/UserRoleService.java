package com.smartwf.sso.service;

import com.smartwf.sso.dto.UserRoleDTO;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/20 16:37
 * @Description: 用户角色业务层接口
 */
public interface UserRoleService {


    /**
     * 保存用户角色
     * @return
     */
    Integer saveUserRole(UserRoleDTO userRoleDTO);

}
