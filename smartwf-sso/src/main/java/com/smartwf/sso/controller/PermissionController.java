package com.smartwf.sso.controller;

import com.smartwf.common.annotation.RequiresPermissions;
import com.smartwf.common.pojo.Result;
import com.smartwf.sso.pojo.Permission;
import com.smartwf.sso.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/29 16:15
 * @Description: 权限
 */
@RestController
@RequestMapping("permission")
@Api(description = "权限控制器")
@Slf4j
public class PermissionController {


    @Autowired
    private PermissionService permissionService;


    /**
     * 根据角色id查询角色权限
     * @return
     */
    @GetMapping("selectPermissionByRoleId/{roleId}")
    @RequiresPermissions("system:role:select")
    @ApiOperation(value="查询角色权限", notes="根据角色id查询角色权限")
    @ApiImplicitParam(paramType="path", name = "roleId", value = "角色id", required = true, dataType = "int")
    public ResponseEntity<Result> selectPermissionByRoleId(@PathVariable("roleId") Integer roleId) {
        List<Permission> permissionList = this.permissionService.selectPermissionByRoleId(roleId);
        if (permissionList != null) {
            return ResponseEntity.ok(Result.data(permissionList));
        }
        log.error("根据角色id查询角色权限错误！");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.msg("根据角色id查询角色权限错误！"));
    }

}
