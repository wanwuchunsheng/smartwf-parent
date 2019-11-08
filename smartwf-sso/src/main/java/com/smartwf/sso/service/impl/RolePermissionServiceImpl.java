package com.smartwf.sso.service.impl;

import com.smartwf.sso.dao.RoleDao;
import com.smartwf.sso.dao.RolePermissionDao;
import com.smartwf.sso.dto.RolePermissionDTO;
import com.smartwf.sso.pojo.Role;
import com.smartwf.sso.pojo.RolePermission;
import com.smartwf.sso.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/21 17:19
 * @Description: 角色权限业务层实现
 */
@Service
public class RolePermissionServiceImpl implements RolePermissionService {


    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Autowired
    private RoleDao roleDao;


    /**
     * 保存角色所拥有的权限
     * @return
     */
    @Override
    @Transactional
    public Integer saveRolePermission(RolePermissionDTO rolePermission) {
        // 确定角色是否可用
        if (checkRole(rolePermission.getRoleId())) {
            return 2;
        }
        // 循环插入父权限以及下面的子权限
        RolePermission rolePer = new RolePermission();
        rolePer.setRoleId(rolePermission.getRoleId());
        // 先删除
        this.rolePermissionDao.delete(rolePer);
        List<Integer> permissionIds = rolePermission.getPermissionIds();
        for (Integer permissionId : permissionIds) {
            rolePer.setPermissionId(permissionId);
            this.rolePermissionDao.insertSelective(rolePer);
        }
        return 1;
    }


    /**
     * 校验角色是否可用
     * @return
     */
    private boolean checkRole(Integer roleId) {
        // 保存之前确定角色是否可用
        Role role = this.roleDao.selectByPrimaryKey(roleId);
        if (role.getEnable() == 0) {
            return true;
        }
        return false;
    }


}
