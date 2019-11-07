package com.smartwf.sso.controller;

import com.smartwf.common.annotation.RequiresPermissions;
import com.smartwf.common.annotation.TraceLog;
import com.smartwf.common.pojo.Result;
import com.smartwf.sso.dto.RolePermissionDTO;
import com.smartwf.sso.service.RolePermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/21 17:15
 * @Description: 角色权限控制器
 */
@RestController
@RequestMapping("rolePermission")
@Slf4j
@Api(description = "角色权限控制器")
public class RolePermissionController {


    @Autowired
    private RolePermissionService rolePermissionService;


    /**
     * 保存角色所拥有的权限
     * @return
     */
    @PostMapping("saveRolePermission")
    @RequiresPermissions("system:role:binding")
    @ApiOperation(value="保存角色所拥有的权限接口", notes="保存角色所拥有的权限")
    @TraceLog(content = "保存角色所拥有的权限", paramIndexs = {0})
    public ResponseEntity<Result> saveRolePermission(@RequestBody RolePermissionDTO rolePermission) {
        try {
            if (rolePermission.getRoleId() == 1) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Result.msg("不能保角色admin！"));
            }
            Integer count = this.rolePermissionService.saveRolePermission(rolePermission);
            if (count == 2) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Result.msg("角色不可用！"));
            }
            return ResponseEntity.ok(Result.successMsg());
        } catch (Exception e) {
            log.error("保存或修改角色所拥有的权限错误！{}", e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.msg("保存或修改角色所拥有的权限错误！"));
    }


}
