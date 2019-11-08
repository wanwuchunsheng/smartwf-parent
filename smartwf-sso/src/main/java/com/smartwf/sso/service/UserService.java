package com.smartwf.sso.service;


import com.smartwf.common.pojo.AppResult;
import com.smartwf.common.pojo.Page;
import com.smartwf.common.pojo.Result;
import com.smartwf.sso.pojo.User;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/17 15:02
 * @Description: 用户业务层接口
 */
public interface UserService {

    /**
     * 分页查询用户
     * @param page
     * @return
     */
    Result selectUserByPage(Page page, User user);


    /**
     * 根据id修改用户信息
     * @return
     */
    Integer updateUserById(User user);


    /**
     * 校验用户名
     * @param user
     * @return
     */
    User selectByUsername(User user);


    /**
     * 保存用户
     * @param user
     * @return
     */
    Integer saveUser(User user);


    /**
     * 删除用户
     * @param userId
     * @return
     */
    Integer deleteUser(Integer userId);


    /**
     * 修改密码
     * @return
     */
    Integer updatePassword(User user);


    /**
     * 登录
     * @param user
     * @return
     */
    ResponseEntity<Result> login(User user, HttpServletRequest request, HttpServletResponse response);


    /**
     * 修改用户
     * @param companyId
     * @return username
     */
    boolean updateUser(Integer companyId, String roleIds);


    /**
     * 查询用户roleIds
     * @param companyId
     * @return
     */
    String selectRoleIds(Integer companyId);


    /**
     * 根据企业id查询企业管理员
     * @param companyId
     * @return 用户名
     */
    String selectByCompanyId(Integer companyId);


    /**
     * 修改用户的有效值
     * @param companyId
     * @return
     */
    boolean updateUserByCompanyId(Integer companyId);


    /**
     * app登录
     * @param user
     * @param request
     * @param response
     * @return
     */
    AppResult appLogin(User user, HttpServletRequest request, HttpServletResponse response);
}
