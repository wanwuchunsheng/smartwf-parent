package com.smartwf.sso.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/17 14:07
 * @Description: 用户
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Data
@Table(name = "sys_user")
@JsonIgnoreProperties("password")
public class User implements Serializable {

    private static final long serialVersionUID = -4091652444259368024L;

    /**
     * id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空！")
    private String username;

    /**
     * 密码
     */

    @NotEmpty(message = "密码不能为空！")
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
     * 查询标识 0: isAdmin[1,2] 1：isAdmin[3,4]
     */
    @Transient
    private Integer selectFlag;


    /**
     * 门店id
     */
    private Integer shopId;


    /**
     * 门店名称
     */
    private String shopName;


    /**
     * 登陆标识 1 南京菜市场 0 食安先
     */
    private Integer loginFlag;
}
