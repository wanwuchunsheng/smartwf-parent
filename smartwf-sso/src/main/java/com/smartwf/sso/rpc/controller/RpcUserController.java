package com.smartwf.sso.rpc.controller;

import com.smartwf.sso.pojo.User;
import com.smartwf.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/20 10:58
 * @Description: 用户控制器
 */
@RestController()
@RequestMapping("rpc/user")
public class RpcUserController {

    @Autowired
    private UserService userService;


    /**
     * 校验用户
     *
     * @return
     */
    @GetMapping("checkUser")
    public boolean checkUser(String username) {
        User user = new User();
        user.setUsername(username);
        User users = this.userService.selectByUsername(user);
        if (users == null) {
            return true;
        }
        return false;
    }


    /**
     * 修改用户
     *
     * @return username
     */
    @GetMapping("updateUser")
    public boolean updateUser(Integer companyId, String roleIds) {
        return this.userService.updateUser(companyId, roleIds);
    }


    /**
     * 查询用户roleIds
     * @param companyId
     * @return
     */
    @GetMapping("selectRoleIds")
    public String selectRoleIds(Integer companyId){
        return this.userService.selectRoleIds(companyId);
    }


    /**
     * 根据企业id查询企业管理员
     * @param companyId
     * @return 用户名
     */
    @GetMapping("selectByCompanyId")
    public String selectByCompanyId(Integer companyId){
        return this.userService.selectByCompanyId(companyId);
    }


    /**
     * 修改用户的有效值
     * @param companyId
     * @return
     */
    @GetMapping("updateUserByCompanyId")
    public boolean updateUserByCompanyId(Integer companyId){
        return this.userService.updateUserByCompanyId(companyId);
    }

}
