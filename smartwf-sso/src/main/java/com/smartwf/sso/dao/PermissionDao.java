package com.smartwf.sso.dao;

import com.smartwf.sso.pojo.Permission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/19 11:16
 * @Description: 权限持久层接口
 */
@Repository
public interface PermissionDao extends Mapper<Permission> {

    /**
     * 根据用户查询相关权限
     * @param userId
     * @return
     */
    Set<Permission> loadUserPermission(Integer userId);


    /**
     * 根据角色id查询角色权限
     * @param roleId
     * @return
     */
    List<Permission> selectPermissionByRoleId(Integer roleId);


    /**
     * 根据用户id跟父id查询相关权限
     * @param userId
     * @return
     */
    Set<Permission> loadUserPermissionByPatentId(@Param("userId") Integer userId, @Param("parentId") Integer parentId);
}
