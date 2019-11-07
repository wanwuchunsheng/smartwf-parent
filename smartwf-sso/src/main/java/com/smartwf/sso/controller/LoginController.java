package com.smartwf.sso.controller;

import com.smartwf.common.annotation.TraceLog;
import com.smartwf.common.pojo.AppResult;
import com.smartwf.common.pojo.Result;
import com.smartwf.sso.pojo.User;
import com.smartwf.sso.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/18 17:32
 * @Description: 登录
 */
@Controller
@Slf4j
@Api(description = "登录控制器")
public class LoginController {


    @Autowired
    private UserService userService;

    /**
     * 登录
     *
     * @param user
     * @return
     */
    @RequestMapping("/login")
    @ApiOperation(value = "登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "password", value = "密码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "loginFlag", value = "登陆标识 1 南京菜市场 0 食安先", required = true, dataType = "int")
    })
    @TraceLog(content = "登录", paramIndexs = {0})
    public ResponseEntity<Result> login(User user, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
                return ResponseEntity.badRequest().body(Result.msg("用户名或密码不能为空！"));
            }
            return this.userService.login(user, request, response);
        } catch (Exception e) {
            log.error("登录错误！{}", e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.msg("登录错误！"));
    }


    /**
     * app登录
     *
     * @param user
     * @return
     */
    @PostMapping("/app/login")
    @ApiOperation(value = "app登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "password", value = "密码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "companyId", value = "企业id", required = true, dataType = "String")
    })
    @TraceLog(content = "app登录", paramIndexs = {0})
    public ResponseEntity<AppResult> appLogin(User user, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return ResponseEntity.ok(AppResult.msg("用户名或密码不能为空！"));
        }
        if (null == user.getCompanyId()) {
            return ResponseEntity.ok(AppResult.msg("企业不能为空！"));
        }
        AppResult result = this.userService.appLogin(user, request, response);
        return ResponseEntity.ok(result);
    }

}
