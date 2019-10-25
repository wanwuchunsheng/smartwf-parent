package com.smartwf.hm.sys.dao;

import org.springframework.stereotype.Repository;

import com.smartwf.hm.sys.pojo.SysUser;

import tk.mybatis.mapper.common.Mapper;


/**
 * @Date: 2019-10-25 15:15:54
 * @Description: 系统管理持久层接口
 */
@Repository
public interface SysUserDao extends Mapper<SysUser> {
}
