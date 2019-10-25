package com.smartwf.common.pojo;

import lombok.Data;

import java.util.Date;

/**
* @Description: 
* @CreateDate: 2018/11/12 10:22
*/
@Data
public class Company {
    /**
     *公司ID
     * */
    private Integer id;


    /**
     *企业名称
     * */
    private String name;


    /**
     *统一社会代码/证件号码
     * */
    private String code;


    /**
     *企业节点码
     * */
    private String companyCode;


    /**
     *中信节点码
     * */
    private String cesgroupCode;


    /**
     *注册地址
     * */
    private String registeredAddressCode;


    /**
     *注册地址
     * */
    private String registeredAddress;


    /**
     *经营地址
     * */
    private String businessAddressCode;


    /**
     *经营地址
     * */
    private String businessAddress;


    /**
     *法人代表
     * */
    private String corporate;


    /**
     *联系人
     * */
    private String contacts;


    /**
     *联系电话
     * */
    private String tel;


    /**
     *企业类型
     * */
    private String companyType;


    /**
     *主营业务
     * */
    private String mainBusiness;


    /**
     *经营领域
     * */
    private String scope;


    /**
     *企业简介
     * */
    private String companyProfiles;


    /**
     *审核状态  1：待审核  2：审核通过  3：已驳回
     * */
    private Integer status;


    /**
     *是否有效  1：有效  0：无效
     * */
    private Integer enable;


    /**
     *到期时间
     * */
    private Date validTime;


    /**
     *logo图片
     * */
    private String logo;


    /**
     *营业执照图片
     * */
    private String license;


    /**
     *父id（母公司的id）
     * */
    private Integer parentId;


    /**
     *门店业态类型
     * */
    private String shopType;


    /**
     *是否直营。1：是，0：否
     * */
    private Integer isDirect;


    /**
     *酒类经营许可证号
     * */
    private String liquorNo;


    /**
     *酒类经营许可证照片
     * */
    private String liquorLicense;

}
