package com.smartwf.sso.controller;

import com.smartwf.common.annotation.RequiresPermissions;
import com.smartwf.common.annotation.TraceLog;
import com.smartwf.common.pojo.Page;
import com.smartwf.common.pojo.Result;
import com.smartwf.common.thread.UserThreadLocal;
import com.smartwf.common.utils.MD5Utils;
import com.smartwf.sso.pojo.User;
import com.smartwf.sso.service.UserService;
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

/**
 * @Date: 2018/9/17 14:57
 * @Description: 用户控制层
 */
@RestController
@RequestMapping("user")
@Slf4j
@Api(description = "用户控制器")
public class UserController {


    @Autowired
    private UserService userService;


    /**
     * 分页查询用户
     *
     * @param page
     * @return
     */
    @GetMapping("/selectUserByPage")
    @RequiresPermissions("system:user:select")
    @ApiOperation(value="分页查询用户接口", notes="根据用户权限分页查询用户（超级管理员可以查询所有用户，管理员则查询除了超级管理员之外的所有用户，普通用户只能看见自己的子用户）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "companyId", value = "企业id", dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "shopId", value = "门店id", dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "username", value = "用户名、昵称、企业名称模糊查询", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "pageNum", value = "要查看的页码，默认是1", dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "pageSize", value = "每页查询数量，默认是10", dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "selectFlag", value = "查询标识 0: isAdmin[1,2] 1：isAdmin[3,4]", dataType = "int")
    })
    public ResponseEntity<Result> selectUserByPage(Page page, User user) {
        try {
            Result result = this.userService.selectUserByPage(page, user);
            if (result != null) {
                return ResponseEntity.ok(result);
            }
        } catch (Exception e) {
            log.error("分页查询用户错误！{}", e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.msg("分页查询用户错误！"));
    }


    /**
     * 根据id修改用户信息
     *
     * @return
     */
    @PutMapping("/updateUserById")
    @RequiresPermissions("system:user:update")
    @ApiOperation(value="修改用户信息接口", notes="根据id修改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "id", value = "用户id", dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "username", value = "用户名", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "password", value = "密码", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "nickName", value = "昵称", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "companyId", value = "所属企业id", dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "companyName", value = "所属企业名称", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "isAdmin", value = "是否为管理员  1：超级管理员（此账号为数据库手动所建），2：平台管理员，3：企业管理员，4：普通用户。默认是普通用户", dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "enable", value = "是否有效  1：有效  0：无效。默认有效", dataType = "int")
    })
    @TraceLog(content = "根据id修改用户信息", paramIndexs = {0})
    public ResponseEntity<Result> updateUserById(User user) {
        try {
            if (user.getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.msg("没有这个用户id！"));
            }
            Integer count = this.userService.updateUserById(user);
            if (count != null && count == 1 || (count == 0)) {
                return ResponseEntity.ok(Result.successMsg());
            }
        } catch (Exception e) {
            log.error("根据id修改用户信息错误！{}", e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.failMsg());
    }


    /**
     *
     * 保存用户
     *
     * @param user
     * @return
     */
    @PostMapping("/saveUser")
    @RequiresPermissions("system:user:save")
    @ApiOperation(value="保存用户接口", notes="保存用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "password", value = "密码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "nickName", value = "昵称", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "companyId", value = "所属企业id", dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "companyName", value = "所属企业名称", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "shopId", value = "门店id", dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "shopName", value = "门店名称", dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "email", value = "邮箱", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "tel", value = "电话", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "isAdmin", value = "是否为管理员  1：超级管理员（此账号为数据库手动所建），2：平台管理员，3：企业管理员，4：普通用户。默认是普通用户", dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "enable", value = "是否有效  1：有效  0：无效。默认有效", dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "loginFlag", value = "登陆标识 1 南京菜市场 0 食安先", dataType = "int")
    })
    @TraceLog(content = "保存用户", paramIndexs = {0})
    public ResponseEntity<Result> saveUser(@Valid User user, BindingResult result) {
        try {
            Integer count = this.userService.saveUser(user);
            if (count != null && count == 1) {
                return ResponseEntity.ok(Result.successMsg());
            } else if (count == 2) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Result.msg("用户名已存在，不可用！"));
            }
        } catch (Exception e) {
            log.error("保存用户信息错误！{}", e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.failMsg());
    }


    /**
     * 删除用户
     * @param user
     * @return
     */
    @DeleteMapping("deleteUser")
    @RequiresPermissions("system:user:delete")
    @ApiOperation(value="删除用户接口", notes="根据id删除用户")
    @ApiImplicitParam(paramType="query", name = "id", value = "用户id", required = true, dataType = "int")
    @TraceLog(content = "删除用户", paramIndexs = {0})
    public ResponseEntity<Result> deleteUser(User user) {
        try {
            if (user.getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.msg("没有用户id！"));
            }
            if (user.getId() == 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.msg("超级管理员不允许删除！"));
            }
            Integer count = this.userService.deleteUser(user.getId());
            if (count != null && count == 1) {
                return ResponseEntity.ok(Result.successMsg());
            }
        } catch (Exception e) {
            log.error("删除用户错误！{}", e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.msg("删除用户错误！"));
    }


    /**
     * 修改密码
     * @param user
     * @return
     */
    @PutMapping("updatePassword")
    @RequiresPermissions("system:user:update")
    @ApiOperation(value="修改用户密码接口", notes="修改用户密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "id", value = "用户id", required = true, dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "password", value = "密码", required = true, dataType = "String")
    })
    @TraceLog(content = "用户修改密码", paramIndexs = {0})
    public ResponseEntity<Result> updatePassword(User user) {
        try {
            if (user.getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.msg("没有用户id！"));
            }
            if (user.getPassword() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.msg("请输入密码！"));
            }
            Integer count = this.userService.updatePassword(user);
            if (count != null && count == 1) {
                return ResponseEntity.ok(Result.successMsg());
            }
        } catch (Exception e) {
            log.error("修改密码错误！{}", e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.msg("修改密码错误！"));
    }


    /**
     * 校验用户名
     * @param user
     * @return
     */
    @GetMapping("selectByUsername")
    @ApiOperation(value="校验用户名接口", notes="校验用户名")
    @ApiImplicitParam(paramType="query", name = "username", value = "用户名", required = true, dataType = "String")
    public ResponseEntity<Result> selectByUsername(User user) {
        try {
            User u = this.userService.selectByUsername(user);
            if (u == null) {
                return ResponseEntity.ok(Result.successMsg());
            } else {
                return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body(Result.failMsg());
            }
        } catch (Exception e) {
            log.error("校验用户名错误！{}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.msg("校验用户名错误！"));
        }
    }

    /**
     * 校验用户名
     * @param password
     * @return
     */
    @GetMapping("checkPassword")
    @ApiOperation(value="校验密码接口", notes="校验密码")
    @ApiImplicitParam(paramType="query", name = "password", value = "密码", required = true, dataType = "String")
    public ResponseEntity<Result> checkPassword(String password) {
        try {
            com.smartwf.common.pojo.User user = UserThreadLocal.getUser();
            String passwords = MD5Utils.md5(password);
            Boolean flag= user.getPassword().equals(passwords);
            return ResponseEntity.ok(Result.data(flag));
        } catch (Exception e) {
            log.error("校验密码错误！{}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.msg("校验密码错误！"));
        }
    }
}
