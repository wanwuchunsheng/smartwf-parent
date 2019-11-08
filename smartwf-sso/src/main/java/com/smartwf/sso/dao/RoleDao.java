package com.smartwf.sso.dao;

import com.smartwf.sso.pojo.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/19 11:06
 * @Description: 角色持久层接口
 */
@Repository
public interface RoleDao extends Mapper<Role> {

    /**
     * 根据用户id查询所拥有的角色
     * @param userId
     * @return
     */
    List<Role> selectRoleByUserId(Integer userId);


    /**
     * 根据用户id查询所拥有的角色
     * @param userId
     * @param systemRole 是否为系统角色  1：是  0：否
     * @return
     */
    List<Role> selectRoleBySys(@Param("userId") Integer userId, @Param("systemRole")Integer systemRole, @Param("roleName")String roleName);
}
