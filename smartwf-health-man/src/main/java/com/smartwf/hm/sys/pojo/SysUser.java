package com.smartwf.hm.sys.pojo;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @Description: 系统用户表
 */
@Data
@Table(name = "sys_user")
public class SysUser {
	/**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
	private Integer id;
    /**
     * 用户名
     */
	private String name;
	/**
     * 密码
     */
	private String password;
	/**
     * 年龄
     */
	private Integer age;
	/**
     * 地址
     */
	private String address;
	/**
     *TEL
     */
	private String tel;
	/**
     *创建时间
     */
	private Date createTime;
	/**
     *创建人ID
     */
	private Integer createUserId;
	/**
     *创建人
     */
	private String createUserName;
	
}
