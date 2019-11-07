package com.smartwf.sso.dao;

import com.smartwf.sso.pojo.UserRole;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/19 11:15
 * @Description: 用户角色持久层接口
 */
@Repository
public interface UserRoleDao extends Mapper<UserRole> {
}
