package com.smartwf.hm.sys.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartwf.common.annotation.TraceLog;
import com.smartwf.common.pojo.Page;
import com.smartwf.common.pojo.Result;
import com.smartwf.hm.sys.pojo.SysUser;
import com.smartwf.hm.sys.service.SysUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
/**
 * 说明：springBoot 用户增删改测试类
 * @author WCH
 * @date 2019年10月23日14:41:54
 * 
 * */
@RestController
@RequestMapping("user")
@Slf4j
@Api(description = "用户管理")
public class UserController {
	
	@Autowired
	private SysUserService sysUserService;
	
	/**
     * 分页查询用户
     *
     * @param page
     * @return
     */
    @GetMapping("selectSysUserByPage")
    @ApiOperation(value = "分页查询用户接口", notes = "分页查询用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "用户名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "startTime", value = "开始时间", dataType = "Date"),
            @ApiImplicitParam(paramType = "query", name = "endTime", value = "结束时间", dataType = "Date"),
            @ApiImplicitParam(paramType = "query", name = "pageNum", value = "要查看的页码，默认是1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页查询数量，默认是10", dataType = "int")
    })
    public ResponseEntity<Result> selectSysUserByPage(Page page, SysUser sysUser) {
        try {
            Result<?> result = this.sysUserService.selectSysUserByPage(page, sysUser);
            if (result != null) {
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
        } catch (Exception e) {
            log.error("分页查询用户信息错误！{}", e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.msg("分页查询用户信息错误！"));
    }
    
    /**
     * 主键查询用户
     * @return
     */
    @GetMapping("selectSysUserById")
    @ApiOperation(value = "主键查询用户接口", notes = "主键查询用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "主键", dataType = "int",required = true)
    })
    public ResponseEntity<Result> selectSysUserById(SysUser sysUser) {
        try {
            Result<?> result = this.sysUserService.selectSysUserById(sysUser);
            if (result != null) {
                return ResponseEntity.ok(result);
            }
        } catch (Exception e) {
            log.error("查询用户信息错误！{}", e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.msg("查询用户信息错误！"));
    }
    
    /**
     * 添加用户
     *
     */
    @PostMapping("saveSysUser")
    @ApiOperation(value = "添加用户接口", notes = "保存用户")
    @ApiImplicitParams({
    	    @ApiImplicitParam(paramType = "query", name = "id", value = "ID", dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "用户名称", dataType = "String", required = true),
    	    @ApiImplicitParam(paramType = "query", name = "age", value = "年龄", dataType = "int", required = true),
    	    @ApiImplicitParam(paramType = "query", name = "address", value = "地址", dataType = "String"),
    	    @ApiImplicitParam(paramType = "query", name = "tel", value = "电话", dataType = "String")
    })
    public ResponseEntity<Result> saveSysUser(HttpSession session,SysUser sysUser) {
        try {
        	Result<?> result= (Result<?>) session.getAttribute("userInfo");
        	SysUser userInfo=(SysUser) result.getData();
        	if(userInfo!=null) {
        		sysUser.setCreateTime(new Date());
        		sysUser.setCreateUserId(userInfo.getId());
        		sysUser.setCreateUserName(userInfo.getName());
        		this.sysUserService.saveSysUser(sysUser);
                return ResponseEntity.status(HttpStatus.OK).body(Result.msg("成功"));
        	}else {
        		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.msg("未登录，请登录后添加！"));
        	}
        } catch (Exception e) {
            log.error("添加用户信息错误！{}", e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.msg("添加用户信息错误！"));
    }
    
    /**
     * 修改用户
     *
     */
    @PutMapping("updateSysUser")
    @ApiOperation(value = "修改用户接口", notes = "修改用户")
    @ApiImplicitParams({
    	    @ApiImplicitParam(paramType = "query", name = "id", value = "ID", dataType = "int",required = true),
            @ApiImplicitParam(paramType = "query", name = "name", value = "用户名称", dataType = "String"),
    	    @ApiImplicitParam(paramType = "query", name = "age", value = "年龄", dataType = "Integer"),
    	    @ApiImplicitParam(paramType = "query", name = "address", value = "地址", dataType = "String"),
    	    @ApiImplicitParam(paramType = "query", name = "tel", value = "电话", dataType = "String")
    })
    @TraceLog(content = "修改系统用户", paramIndexs = {0})
    public ResponseEntity<Result> updateSysUser(SysUser sysUser) {
        try {
            this.sysUserService.updateSysUser(sysUser);
            return ResponseEntity.status(HttpStatus.OK).body(Result.msg("成功"));
        } catch (Exception e) {
            log.error("修改用户信息错误！{}", e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.msg("修改用户信息错误！"));
    }
    
    /**
     * 删除用户
     * 
     */
    @DeleteMapping("deleteSysUser")
    @ApiOperation(value = "删除用户接口", notes = "删除用户")
    @ApiImplicitParams({
    	    @ApiImplicitParam(paramType = "query", name = "id", value = "id", dataType = "int",required = true)
    })
    @TraceLog(content = "修改系统用户", paramIndexs = {0})
    public ResponseEntity<Result> deleteSysUser(SysUser sysUser) {
        try {
        	this.sysUserService.deleteSysUser(sysUser);
            return ResponseEntity.status(HttpStatus.OK).body(Result.msg("成功"));
        } catch (Exception e) {
            log.error("删除用户信息错误！{}", e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.msg("删除用户信息错误！"));
    }

}
