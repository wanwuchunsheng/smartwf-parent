package com.smartwf.sso.service.impl;

import com.smartwf.sso.dao.RoleDao;
import com.smartwf.sso.dao.RolePermissionDao;
import com.smartwf.sso.pojo.Role;
import com.smartwf.sso.pojo.RolePermission;
import com.smartwf.sso.service.RoleService;
import com.github.pagehelper.PageHelper;
import com.smartwf.common.constant.Constants;
import com.smartwf.common.exception.CommonException;
import com.smartwf.common.pojo.Page;
import com.smartwf.common.pojo.Result;
import com.smartwf.common.pojo.User;
import com.smartwf.common.thread.UserThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/20 17:32
 * @Description: 角色业务层实现
 */
@Service
public class RoleServiceImpl implements RoleService {


    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;


    /**
     * 保存、修改角色
     *
     * @param role
     * @return
     */
    @Transactional
    @Override
    public Integer saveOrUpdateRole(Role role) {
        // 判断角色名称不能重复
        Role queryRole = new Role();
        queryRole.setRoleName(role.getRoleName());
        if (role.getId() == null) {
            // 给角色赋值企业id
            User user = UserThreadLocal.getUser();
            if (user.getCompanyId() != null) {
                queryRole.setCompanyId(user.getCompanyId());
                role.setCompanyId(user.getCompanyId());
            } else if (user.getIsAdmin() == 1) {
                // 超级管理员创建的角色企业为-1
                queryRole.setCompanyId(-1);
                role.setCompanyId(-1);
            } else if (user.getIsAdmin() == 2) {
                // 平台管理员创建的角色企业为-2
                queryRole.setCompanyId(-2);
                role.setCompanyId(-2);
            }
            if (role.getEnable() == null) {
                role.setEnable(1);
            }
            //赋值不同的企业id查询角色名称是否重复
            List<Role> roles = this.roleDao.select(queryRole);
            if (!roles.isEmpty()) {
                return 2;
            }
            return this.roleDao.insertSelective(role);
        } else {
            return this.roleDao.updateByPrimaryKeySelective(role);
        }
    }


    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    @Override
    @Transactional
    public Integer deleteRole(Integer roleId) throws RuntimeException {
        int count = this.roleDao.deleteByPrimaryKey(roleId);
        if (count == 1) {
            // 级联删除这个角色所拥有的权限
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            this.rolePermissionDao.delete(rolePermission);
            return count;
        } else {
            throw new CommonException(Constants.INTERNAL_SERVER_ERROR, "删除角色错误！");
        }
    }


    /**
     * 分页查询角色
     *
     * @param page
     * @return
     */
    @Override
    public Result selectRoleByPage(Page page, Role role) {
        // 获取当前登录用户
        User user = UserThreadLocal.getUser();
        List<Role> roleList = null;
        com.github.pagehelper.Page<Object> pages = PageHelper.startPage(page.getPage(), page.getLimit());

        Example example = new Example(Role.class);
        example.setOrderByClause("id desc");
        Example.Criteria criteria = example.createCriteria();
        // 查询系统角色
        if (role.getSystemRole() != null) {
            criteria.andEqualTo("systemRole", role.getSystemRole());
            roleList = this.roleDao.selectByExample(example);
            return Result.data(pages.getTotal(), roleList);
        }
        // 根据角色名称查询角色
        // 超级管理员
        if (StringUtils.isNotBlank(role.getRoleName()) && user.getIsAdmin() == 1) {
            criteria.andLike("roleName", Constants.PER_CENT + role.getRoleName() + Constants.PER_CENT);
            roleList = this.roleDao.selectByExample(example);
            return Result.data(pages.getTotal(), roleList);
        }
        // 平台管理员
        if (StringUtils.isNotBlank(role.getRoleName()) && user.getIsAdmin() == 2) {
            criteria.andLike("roleName", Constants.PER_CENT + role.getRoleName() + Constants.PER_CENT);
            roleList = this.roleDao.selectByExample(example);
            Iterator<Role> iterator = roleList.iterator();
            while (iterator.hasNext()) {
                Role next = iterator.next();
                if (next.getId() == 1) {
                    iterator.remove();
                    break;
                }
            }
            return Result.data(pages.getTotal(), roleList);
        }
        // 企业管理员
        if (StringUtils.isNotBlank(role.getRoleName()) && user.getIsAdmin() == 3) {
            criteria.andEqualTo("companyId", user.getCompanyId());
            criteria.andEqualTo("systemRole", 0);
            criteria.andLike("roleName", Constants.PER_CENT + role.getRoleName() + Constants.PER_CENT);
            roleList = this.roleDao.selectByExample(example);
            return Result.data(pages.getTotal(), roleList);
        }
        // 普通用户
        if (StringUtils.isNotBlank(role.getRoleName()) && user.getIsAdmin() == 4) {
            roleList = this.roleDao.selectRoleBySys(user.getId(), 0, role.getRoleName());
            return Result.data(pages.getTotal(), roleList);
        }

        if (user.getIsAdmin() == 1) {
            // 超级管理员查询所有角色
            roleList = this.roleDao.selectAll();
        } else if (user.getIsAdmin() == 2) {
            // 平台管理员能看见除了超级管理员之外的所有角色
            roleList = this.roleDao.selectAll();
            Iterator<Role> iterator = roleList.iterator();
            while (iterator.hasNext()) {
                Role next = iterator.next();
                if (next.getId() == 1) {
                    iterator.remove();
                    break;
                }
            }
        } else if (user.getIsAdmin() == 3) {
            // 企业管理员只能查询自己企业下面的角色，不能查询到系统角色
            criteria.andEqualTo("companyId", user.getCompanyId());
            criteria.andEqualTo("systemRole", 0);
            roleList = this.roleDao.selectByExample(example);
            return Result.data(pages.getTotal(), roleList);
        } else if (user.getIsAdmin() == 4) {
            // 普通用户只能查询自己所拥有的角色，不能查询到系统角色
            roleList = this.roleDao.selectRoleBySys(user.getId(), 0, null);
        }
        return Result.data(pages.getTotal(), roleList);
    }


    /**
     * 根据用户查询角色
     *
     * @param userId
     * @return
     */
    @Override
    public Map<String, List<Role>> selectRoleByUserId(Integer userId) {
        Map<String, List<Role>> roleMap = new HashMap<>();
        List<Role> roleList = new ArrayList<>();
        // 获取当前登录用户
        User loginUser = UserThreadLocal.getUser();
        // 查询未拥有角色
        Role queryRole = new Role();
        // 登录用户的角色集合
        if (loginUser.getCompanyId() != null) {
            queryRole.setCompanyId(loginUser.getCompanyId());
            roleList = this.roleDao.select(queryRole);
        }
        if (loginUser.getIsAdmin() == 1) {
            // 超级管理员创建的角色企业为-1，但是超级管理员理应看到所有角色
            roleList = this.roleDao.selectAll();
        } else if (loginUser.getIsAdmin() == 2) {
            // 平台管理员创建的角色企业为-2
            queryRole.setCompanyId(-2);
            roleList = this.roleDao.select(queryRole);
        }
        // 获取已拥有角色
        List<Role> roles = new ArrayList<>();
        if (userId != null) {
            roles = this.roleDao.selectRoleByUserId(userId);
        }
        // 去除已拥有的角色
        Collection coll = new ArrayList<>(roleList);
        coll.removeAll(roles);
        List<Role> list = (List<Role>) coll;
        roleMap.put("not", list);
        roleMap.put("have", roles);
        return roleMap;
    }


    /**
     * 示范性菜市场分页查询企业角色
     *
     * @param page
     * @param role
     * @return
     */
    @Override
    public Result selectRoleInfoByPage(Page page, Role role) {
    	com.github.pagehelper.Page<Object> pages = PageHelper.startPage(page.getPage(), page.getLimit());
        Example example = new Example(Role.class);
        example.setOrderByClause("id desc");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("companyId", role.getCompanyId());
        if (role.getRoleName() != null) {
            criteria.andLike("roleName", Constants.PER_CENT + role.getRoleName() + Constants.PER_CENT);
        }
        List<Role> roleList = this.roleDao.selectByExample(example);
        return Result.data(pages.getTotal(), roleList);
    }
}
