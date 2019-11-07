package com.smartwf.sso.dao;

import com.smartwf.sso.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Auther: 赵明明
 * @Date: 2018/9/17 15:05
 * @Description: 用户持久层接口
 */
@Repository
public interface UserDao extends Mapper<User> {


    /**
     * 根据用户名、昵称、企业名称模糊查询
     *
     * @param name
     * @return
     */
    List<User> selectByLike(@Param("name") String name, @Param("companyId") Integer companyId, @Param("shopId") Integer shopId);


    /**
     * 根据isAdmin模糊查询
     *
     * @param name
     * @param isAdmin
     * @return
     */
    List<User> selectByIsAdmin(@Param("name") String name, @Param("IsAdminList") List<Integer> isAdmin);
}
