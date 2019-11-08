package com.smartwf.sso.service;

import com.smartwf.sso.pojo.Permission;
import com.smartwf.sso.pojo.User;

import java.util.List;
import java.util.Set;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/19 14:40
 * @Description: 权限业务层接口
 */
public interface PermissionService {

    /**
     * 根据用户获取相关权限
     * @param user
     * @return
     */
    Set<Permission> loadUserPermission(User user);


    /**
     * 根据角色id查询角色权限
     * @param roleId
     * @return
     */
    List<Permission> selectPermissionByRoleId(Integer roleId);
}
