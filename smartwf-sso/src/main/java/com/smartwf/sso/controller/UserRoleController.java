package com.smartwf.sso.controller;

import com.smartwf.common.annotation.RequiresPermissions;
import com.smartwf.common.annotation.TraceLog;
import com.smartwf.common.pojo.Result;
import com.smartwf.sso.dto.UserRoleDTO;
import com.smartwf.sso.service.UserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/20 16:33
 * @Description: 用户角色控制器
 */
@RestController
@RequestMapping("userRole")
@Slf4j
@Api(description = "用户角色控制器")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;


    /**
     * 保存用户角色
     *
     * @return
     */
    @PostMapping("saveUserRole")
    @RequiresPermissions("system:user:binding")
    @ApiOperation(value="保存用户角色接口", notes="保存用户角色之间的关联")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "userId", value = "用户id", dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "roleIds", value = "角色id，多个以英文逗号分隔", dataType = "String")
    })
    @TraceLog(content = "保存用户角色", paramIndexs = {0})
    public ResponseEntity<Result> saveUserRole(UserRoleDTO userRoleDTO) {
        try {
            if (userRoleDTO.getUserId() == 1) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Result.msg("不能保存用户admin！"));
            }
            Integer count = this.userRoleService.saveUserRole(userRoleDTO);
            if (count != null) {
                return ResponseEntity.ok(Result.successMsg());
            }
        } catch (Exception e) {
            log.error("保存用户角色失败！{}", e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.msg("保存用户角色失败！"));
    }

}
