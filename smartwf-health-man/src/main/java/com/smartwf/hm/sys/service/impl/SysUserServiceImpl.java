package com.smartwf.hm.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.smartwf.common.constant.Constants;
import com.smartwf.common.pojo.Page;
import com.smartwf.common.pojo.Result;
import com.smartwf.hm.sys.dao.SysUserDao;
import com.smartwf.hm.sys.pojo.SysUser;
import com.smartwf.hm.sys.service.SysUserService;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

/**
 * @Description: 系统业务层实现
 */
@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserDao sysUserDao;
	
	/**
     * 分页查询用户
     *
     * @param page
     * @return
     */
	@Override
	public Result selectSysUserByPage(Page page, SysUser sysUser) {
		com.github.pagehelper.Page<Object> objectPage = PageHelper.startPage(page.getPage(), page.getLimit());
		Example example = new Example(SysUser.class);
        example.setOrderByClause("create_time desc");
        Example.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(sysUser.getName())) {
            criteria.andLike("name", Constants.PER_CENT + sysUser.getName() + Constants.PER_CENT);
        }
        if (!StringUtils.isEmpty(sysUser.getTel())) {
            criteria.andLike("tel", Constants.PER_CENT + sysUser.getTel() + Constants.PER_CENT);
        }
        if (!StringUtils.isEmpty(sysUser.getAddress())) {
            criteria.andLike("address", Constants.PER_CENT + sysUser.getAddress() + Constants.PER_CENT);
        }
		List<SysUser> list=this.sysUserDao.selectByExample(example);
		return Result.data(objectPage.getTotal(), list,0,"");
	}

	/**
     * 添加用户
     *
     * @return
     */
	@Transactional //事务
	@Override
	public void saveSysUser(SysUser sysUser) {
		this.sysUserDao.insertSelective(sysUser);
		log.info("插入成功，返回值:{}",sysUser.getId());
	}

	/**
     * 登录查询
     *
     * @return
     */
	@Override
	public Result queryUserByParam(SysUser user) {
		SysUser userInfo= this.sysUserDao.selectOne(user);
		return Result.data(userInfo);
	}

	/**
     * 修改用户
     *
     */
	@Override
	public void updateSysUser(SysUser sysUser) {
		this.sysUserDao.updateByPrimaryKeySelective(sysUser);
	}

	/**
     * 删除用户
     *
     */
	@Override
	public void deleteSysUser(SysUser sysUser) {
		this.sysUserDao.deleteByPrimaryKey(sysUser);
	}

	/**
     * 主键查询用户
     *
     */
	@Override
	public Result<?> selectSysUserById(SysUser sysUser) {
		SysUser userInfo= this.sysUserDao.selectByPrimaryKey(sysUser);
		return Result.data(userInfo);
	}
}
