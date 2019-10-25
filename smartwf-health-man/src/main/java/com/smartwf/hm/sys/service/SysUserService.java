package com.smartwf.hm.sys.service;

import com.smartwf.common.pojo.Page;
import com.smartwf.common.pojo.Result;
import com.smartwf.hm.sys.pojo.SysUser;

/**
 * @Description: 系统业务层接口
 */
public interface SysUserService {

	/**
     * 分页查询用户
     *
     * @param page
     * @return
     */
	Result selectSysUserByPage(Page page, SysUser sysUser);

	/**
     * 添加用户
     *
     * @return
     */
	void saveSysUser(SysUser sysUser);

	/**
     * 登录查询
     *
     * @return
     */
	Result queryUserByParam(SysUser user);

	/**
     * 修改用户
     *
     * @return
     */
	void updateSysUser(SysUser sysUser);

	/**
     * 删除用户
     *
     * @return
     */
	void deleteSysUser(SysUser sysUser);

	/**
     * 主键查询用户
     *
     * @return
     */
	Result<?> selectSysUserById(SysUser sysUser);

    
}