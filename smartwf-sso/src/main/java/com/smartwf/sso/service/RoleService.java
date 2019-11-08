package com.smartwf.sso.service;

import com.smartwf.common.pojo.Page;
import com.smartwf.common.pojo.Result;
import com.smartwf.sso.pojo.Role;

import java.util.List;
import java.util.Map;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/20 17:29
 * @Description: 角色业务层接口
 */
public interface RoleService {

    /**
     * 保存、修改角色
     * @param role
     * @return
     */
    Integer saveOrUpdateRole(Role role);


    /**
     * 删除角色
     * @param roleId
     * @return
     */
    Integer deleteRole(Integer roleId) throws RuntimeException;


    /**
     * 分页查询角色
     * @param page
     * @return
     */
    Result selectRoleByPage(Page page, Role role);


    /**
     * 根据用户查询角色
     * @param userId
     * @return
     */
    Map<String, List<Role>> selectRoleByUserId(Integer userId);


    /**
     * 示范性菜市场分页查询企业角色
     *
     * @param page
     * @param role
     * @return
     */
    Result selectRoleInfoByPage(Page page,Role role);
}
