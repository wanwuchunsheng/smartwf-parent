package com.smartwf.sso.service.impl;

import com.smartwf.sso.dao.UserRoleDao;
import com.smartwf.sso.dto.UserRoleDTO;
import com.smartwf.sso.pojo.UserRole;
import com.smartwf.sso.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/20 16:38
 * @Description: 用户角色业务层实现
 */
@Slf4j
@Service
public class UserRoleServiceimpl implements UserRoleService {


    @Autowired
    private UserRoleDao userRoleDao;


    /**
     * 保存用户角色中间表
     *
     * @return
     */
    @Override
    @Transactional
    public Integer saveUserRole(UserRoleDTO userRoleDTO) {
        // 先删除之前的全部角色
        UserRole userRole = new UserRole();
        userRole.setUserId(userRoleDTO.getUserId());
        if (StringUtils.isNotBlank(userRoleDTO.getRoleIds())) {
            Integer flag = this.userRoleDao.delete(userRole);
            String roleIdStr = userRoleDTO.getRoleIds();
            String[] roleIds = roleIdStr.split(",");
            for (String roleId : roleIds) {
                int id = Integer.valueOf(roleId);
                userRole.setRoleId(id);
                this.userRoleDao.insertSelective(userRole);
            }
            return flag;
        } else {
            return this.userRoleDao.delete(userRole);
        }
    }

}
