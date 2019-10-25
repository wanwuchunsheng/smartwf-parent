package com.smartwf.common.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @Date: 2018/9/17 14:07
 * @Description: 用户
 */
@Data
public class User {


    /**
     * id
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */

    private String password;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 所属企业id
     */
    private Integer companyId;

    /**
     * 所属企业名称
     */
    private String companyName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话
     */
    private String tel;

    /**
     * 是否为管理员  1：超级管理员（此账号为数据库手动所建），2：平台管理员，3：企业管理员，4：普通用户。默认是普通用户
     */
    private Integer isAdmin;

    /**
     * 最后登录时间
     */
    private Date loginTime;

    /**
     * 是否有效  1：有效  0：无效。默认有效
     */
    private Integer enable;


    /**
     * 门店id
     */
    private Integer shopId;


    /**
     * 门店名称
     */
    private String shopName;


    /**
     * 用户注册标识 1 菜市场 0 食安先
     */
    private Integer loginFlag;
}
