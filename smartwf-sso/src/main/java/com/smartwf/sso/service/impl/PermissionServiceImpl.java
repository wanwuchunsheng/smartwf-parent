package com.smartwf.sso.service.impl;

import com.smartwf.sso.dao.PermissionDao;
import com.smartwf.sso.pojo.Permission;
import com.smartwf.sso.pojo.User;
import com.smartwf.sso.service.PermissionService;
import com.alibaba.fastjson.JSON;
import com.smartwf.common.thread.PermissionThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/19 14:40
 * @Description: 权限业务层实现
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;


    /**
     * 根据用户获取相关权限
     * @param user
     * @return
     */
    @Override
    public Set<Permission> loadUserPermission(User user) {
        if (user == null || StringUtils.isEmpty(user.getId())) {
            return null;
        }
        Set<Permission> permissionList = this.permissionDao.loadUserPermission(user.getId());
        return permissionList;
    }


    /**
     * 根据角色id查询角色权限
     * @param roleId
     * @return
     */
    @Override
    public List<Permission> selectPermissionByRoleId(Integer roleId) {
        // 全部权限
        String permissionJson = PermissionThreadLocal.getPermission();
        List<Permission> permissionList = JSON.parseArray(permissionJson, Permission.class);
        // 拥有的权限
        List<Permission> permissions = this.permissionDao.selectPermissionByRoleId(roleId);
        // 一级权限
        // 回显数据，一级。是否选中
        this.isSelected(permissionList, permissions);
        for (Permission permission : permissionList) {
            // 二级权限
            // 回显数据，二级。是否选中
            this.isSelected(permission.getChildren(), permissions);
            for (Permission permission2 : permission.getChildren()) {
                // 三级权限
                // 回显数据，三级。是否选中
                this.isSelected(permission2.getChildren(), permissions);
            }
        }

        return permissionList;
    }


    /**
     *  回显数据，是否选中
     * @param permissionList 全部权限
     * @param permissions 拥有的权限
     */
    private void isSelected(List<Permission> permissionList, List<Permission> permissions) {
        for (Permission permission : permissions) {
            for (Permission per : permissionList) {
                if (per.getId().equals(permission.getId())) {
                    per.setFlag(true);
                    break;
                }
            }
        }
    }

}
