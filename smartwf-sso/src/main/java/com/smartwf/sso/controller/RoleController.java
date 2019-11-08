package com.smartwf.sso.controller;

import com.smartwf.common.annotation.RequiresPermissions;
import com.smartwf.common.annotation.TraceLog;
import com.smartwf.common.pojo.Logical;
import com.smartwf.common.pojo.Page;
import com.smartwf.common.pojo.Result;
import com.smartwf.sso.pojo.Role;
import com.smartwf.sso.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/20 17:20
 * @Description: 角色控制器
 */
@RestController
@RequestMapping("role")
@Slf4j
@Api(description = "角色控制器")
public class RoleController {


    @Autowired
    private RoleService roleService;


    /**
     * 保存、修改角色
     *
     * @param role
     * @param result
     * @return
     */
    @PostMapping("saveOrUpdateRole")
    @RequiresPermissions(value = {"system:role:save", "system:role:update"}, logical = Logical.OR)
    @ApiOperation(value = "保存、修改角色接口", notes = "传入id就修改，不传入id就添加")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "角色id", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "shopId", value = "门店id", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "roleName", value = "角色名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "角色描述", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "systemRole", value = "是否为系统角色  1：是  0：否", dataType = "int")
    })
    @TraceLog(content = "保存、修改角色", paramIndexs = {0})
    public ResponseEntity<Result> saveOrUpdateRole(@Valid Role role, BindingResult result) {
        try {
            Integer count = this.roleService.saveOrUpdateRole(role);
            if (count != null && count == 1 || (count == 0)) {
                return ResponseEntity.ok(Result.successMsg());
            } else if (count != null && count == 2) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Result.msg("角色名已存在，不可用！"));
            }
        } catch (Exception e) {
            log.error("保存角色错误！{}", e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.msg("保存角色错误！"));
    }


    /**
     * 删除角色
     *
     * @param role
     * @return
     */
    @DeleteMapping("deleteRole")
    @RequiresPermissions("system:role:delete")
    @ApiOperation(value = "删除角色接口", notes = "根据id删除角色")
    @ApiImplicitParam(paramType = "query", name = "id", value = "角色id", required = true, dataType = "int")
    @TraceLog(content = "删除角色", paramIndexs = {0})
    public ResponseEntity<Result> deleteRole(Role role) {
        try {
            if (role.getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.msg("缺少角色id！"));
            }
            if (role.getId() == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Result.msg("不能删除admin！"));
            }
            Integer count = this.roleService.deleteRole(role.getId());
            if (count != null && count == 1) {
                return ResponseEntity.ok(Result.successMsg());
            }
        } catch (Exception e) {
            log.error("删除角色错误！{}", e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.msg("删除角色错误！"));
    }


    /**
     * 分页查询角色
     *
     * @param page
     * @return
     */
    @GetMapping("selectRoleByPage")
    @RequiresPermissions("system:role:select")
    @ApiOperation(value = "分页查询角色", notes = "分页查询角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "roleName", value = "角色名称。不传则查询所有", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "systemRole", value = "是否为系统角色  1：是  0：否", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNum", value = "要查看的页码，默认是1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页查询数量，默认是10", dataType = "int")
    })
    public ResponseEntity<Result> selectRoleByPage(Page page, Role role) {
        try {
            Result result = this.roleService.selectRoleByPage(page, role);
            if (result != null) {
                return ResponseEntity.ok(result);
            }
        } catch (Exception e) {
            log.error("分页查询角色失败！{}", e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.msg("分页查询角色失败！"));
    }


    /**
     * 根据用户查询角色
     *
     * @return
     */
    @GetMapping("selectRoleByUserId")
    @RequiresPermissions("system:role:select")
    @ApiOperation(value = "根据用户查询角色接口", notes = "根据用户查询角色")
    @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "int")
    public ResponseEntity<Result> selectRoleByUserId(Integer userId) {
        try {
            Map<String, List<Role>> roleMap = this.roleService.selectRoleByUserId(userId);
            if (roleMap != null) {
                return ResponseEntity.ok(Result.data(roleMap));
            }
        } catch (Exception e) {
            log.error("根据用户查询角色！{}", e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.msg("根据用户查询角色失败！"));
    }

    /**
     * 示范性菜市场分页查询企业角色
     *
     * @return
     */
    @GetMapping("selectRoleInfoByPage")
    @RequiresPermissions("system:role:select")
    @ApiOperation(value = "示范性菜市场分页查询企业角色接口", notes = "示范性菜市场分页查询企业角色")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "companyId", value = "企业id", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "roleName", value = "角色名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageNum", value = "要查看的页码，默认是1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页查询数量，默认是10", dataType = "int")
    })
    public ResponseEntity<Result> selectRoleInfoByPage(Page page, Role role) {
        try {
            Result result = this.roleService.selectRoleInfoByPage(page, role);
            if (result != null) {
                return ResponseEntity.ok(Result.data(result));
            }
        } catch (Exception e) {
            log.error("示范性菜市场分页查询企业角色失败！{}", e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.msg("示范性菜市场分页查询企业角色失败！"));
    }
}
