package com.smartwf.sso.service.impl;

import com.smartwf.common.constant.Constants;
import com.smartwf.common.pojo.AppResult;
import com.smartwf.common.pojo.Page;
import com.smartwf.common.pojo.Result;
import com.smartwf.common.service.RedisService;
import com.smartwf.common.thread.UserThreadLocal;
import com.smartwf.common.utils.MD5Utils;
import com.smartwf.sso.dao.PermissionDao;
import com.smartwf.sso.dao.UserDao;
import com.smartwf.sso.dao.UserRoleDao;
import com.smartwf.sso.pojo.Permission;
import com.smartwf.sso.pojo.User;
import com.smartwf.sso.pojo.UserRole;
import com.smartwf.sso.service.UserService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Date: 2018/9/17 15:04
 * @Description: 用户业务层实现
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {


    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisService redisService;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private UserRoleDao userRoleDao;

    /**
     * 分页查询用户
     *
     * @param page
     * @return
     */
    @Override
    public Result selectUserByPage(Page page, User user) {
        // 获取当前用户
        com.smartwf.common.pojo.User loginUser = UserThreadLocal.getUser();
        user.setCompanyId(loginUser.getCompanyId());
        // 设置分页
        com.github.pagehelper.Page<Object> objectPage = PageHelper.startPage(page.getPage(), page.getLimit());
        List<User> userList = new ArrayList<>();
        // 0: isAdmin[1,2] 1：isAdmin[3,4]
        List<Integer> isAdmin = new ArrayList<>();
        //如果查询标识不为空则进行赋值
        if (user.getSelectFlag() != null) {
            if (user.getSelectFlag() == 0) {
                isAdmin.add(1);
                isAdmin.add(2);
            } else {
                isAdmin.add(3);
                isAdmin.add(4);
            }
        }
        // 模糊查询
        if (StringUtils.isNotBlank(user.getUsername())) {
            //selectFlag 查询标识 0: isAdmin[1,2] 1：isAdmin[3,4]
            if (user.getSelectFlag() != null) {
                userList = this.userDao.selectByIsAdmin(user.getUsername(), isAdmin);
            } else {
                if (user.getCompanyId() != null) {
                    if (user.getCompanyId() == -1) {
                        user.setCompanyId(null);
                    }
                }
                userList = this.userDao.selectByLike(user.getUsername(), user.getCompanyId(),user.getShopId());
            }
            return Result.data(objectPage.getTotal(), userList);
        }
        if (user.getSelectFlag() != null) {
            userList = this.userDao.selectByIsAdmin(user.getUsername(), isAdmin);
            return Result.data(objectPage.getTotal(), userList);
        }
        /**
         * 管理员权限：超级管理员-->平台管理员-->企业管理员
         */
        Example example = new Example(User.class);
        example.setOrderByClause("id desc,company_id desc");
        if (loginUser.getIsAdmin() == 1) {
            // 如果是超级管理员，则查询所有的用户
            userList = this.userDao.selectByExample(example);
        } else if (loginUser.getIsAdmin() == 2) {
            // 如果是平台管理员，则查询除了超级管理员之外的所有用户
            userList = this.userDao.selectByExample(example);
            Iterator<User> iterator = userList.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getIsAdmin() == 1) {
                    iterator.remove();
                    break;
                }
            }
        } else if (loginUser.getIsAdmin() == 3) {
            // 如果是企业管理员，则查询自己的子用户
            Example.Criteria criteria = example.createCriteria();
            example.setOrderByClause("id desc,company_id desc");
            criteria.orEqualTo("id", loginUser.getId());
            criteria.orEqualTo("companyId", user.getCompanyId());
            userList = this.userDao.selectByExample(example);
        } else if (loginUser.getIsAdmin() == 4) {
            // 不是管理员
            Example.Criteria criteria = example.createCriteria();
            example.setOrderByClause("id desc,company_id desc");
            criteria.orEqualTo("id", loginUser.getId());
            if (Optional.ofNullable(user.getShopId()).isPresent()) {
                criteria.orEqualTo("shopId", user.getShopId());
            }
            userList = this.userDao.selectByExample(example);
        }
        return Result.data(objectPage.getTotal(), userList);
    }


    /**
     * 根据id修改用户信息
     *
     * @return
     */
    @Override
    @Transactional
    public Integer updateUserById(User user) {
        user.setPassword(null);
        user.setCompanyId(null);
        user.setIsAdmin(null);
        return this.userDao.updateByPrimaryKeySelective(user);
    }


    /**
     * 校验用户名
     *
     * @param user
     * @return
     */
    @Override
    public User selectByUsername(User user) {
        return this.userDao.selectOne(user);
    }


    /**
     * 保存用户
     *
     * @param user
     * @return
     */
    @Override
    @Transactional
    public Integer saveUser(User user) {
        User queryUser = new User();
        queryUser.setUsername(user.getUsername());
        User u = this.userDao.selectOne(queryUser);
        if (u != null) {
            return 2;
        }
        // 添加父id
        com.smartwf.common.pojo.User loginUser = UserThreadLocal.getUser();
        if (loginUser != null) {
            user.setParentId(loginUser.getId());
        }
        if (user.getIsAdmin() == null) {
            // 如果为空，则默认不是管理员
            user.setIsAdmin(4);
        }
        if (loginUser.getIsAdmin() == 1) {
            // 如果是超级管理员创建的账号，为管理员。企业id为-2
            user.setCompanyId(-1);
        }
        if (loginUser.getIsAdmin() == 2) {
            // 如果是管理员创建的账号，为平台用户。企业id为-2
            user.setCompanyId(-2);
        }
        // 加密密码
        user.setPassword(MD5Utils.md5(user.getPassword()));
        user.setEnable(1);
        return this.userDao.insertSelective(user);
    }


    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public Integer deleteUser(Integer userId) {
        return this.userDao.deleteByPrimaryKey(userId);
    }


    /**
     * 修改密码
     *
     * @param user
     * @return
     */
    @Override
    @Transactional
    public Integer updatePassword(User user) {
        // 加密密码
        user.setPassword(MD5Utils.md5(user.getPassword()));
        user.setCompanyId(null);
        user.setIsAdmin(null);
        return this.userDao.updateByPrimaryKeySelective(user);
    }


    /**
     * 登录
     *
     * @param user
     * @return
     */
    @Transactional
    @Override
    public ResponseEntity<Result> login(User user, HttpServletRequest request, HttpServletResponse response) {
        user.setPassword(MD5Utils.md5(user.getPassword()));
        user.setIsAdmin(null);
        List<User> userList = this.userDao.select(user);
        if (userList != null && !userList.isEmpty()) {
            User loginUser = userList.get(0);
            if (loginUser.getEnable() == 0) {
                log.warn("用户已经被锁定不能登录，请与管理员联系！用户信息：{}", loginUser);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Result.msg("用户已经被锁定不能登录，请与管理员联系！"));
            }
            // 设置最后登录时间
            user.setId(loginUser.getId());
            user.setLoginTime(new Date());
            this.userDao.updateByPrimaryKeySelective(user);
            // 获取用户信息
            String userJson = JSON.toJSONString(loginUser);
            // 获取权限数据
            List<Permission> permissionList = this.getPermission(loginUser.getId());
            String permissionJson = JSON.toJSONString(permissionList);
            // md5加密时间作为key，用户作为field，权限作为value。存入redis。格式为cookieValue：用户信息：用户权限。为HASH结构
            String key = MD5Utils.md5(System.currentTimeMillis() + loginUser.getUsername());
            this.redisService.hset(key, userJson, permissionJson, 14400);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("user", loginUser);
            resultMap.put("permission", permissionList);
            // 将token返回
            resultMap.put(Constants.SMARTWF_TOKEN, key);
            return ResponseEntity.ok(Result.data(resultMap));
        }
        log.warn("用户或密码不正确！");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result.msg("用户或密码不正确！"));
    }


    /**
     * 修改用户
     *
     * @param companyId
     * @return username
     */
    @Transactional
    @Override
    public boolean updateUser(Integer companyId, String roleIds) {
        User user = new User();
        user.setCompanyId(companyId);
        user.setIsAdmin(3);
        List<User> userList = this.userDao.select(user);
        for (User u : userList) {
            if (u != null) {
                u.setEnable(1);
                user = u;
                this.userDao.updateByPrimaryKeySelective(u);
                if (StringUtils.isNotEmpty(roleIds)) {
                    // 保存用户的系统角色
                    String[] roleArr = roleIds.split(",");
                    UserRole userRole = new UserRole();
                    userRole.setUserId(user.getId());
                    //先根据id删除原有的roleIds
                    this.userRoleDao.delete(userRole);
                    for (String roleId : roleArr) {
                        userRole.setRoleId(Integer.valueOf(roleId));
                        this.userRoleDao.insertSelective(userRole);
                    }
                }
            }
        }
        return true;
    }

    /**
     * 查询用户roleIds
     *
     * @param companyId
     * @return
     */
    @Transactional
    @Override
    public String selectRoleIds(Integer companyId) {
        User user1 = new User();
        user1.setCompanyId(companyId);
        user1.setIsAdmin(3);
        User user = this.userDao.selectOne(user1);
        Set<Integer> integerSet = new HashSet<>();
        String roleIds = "";
        if (user != null) {
            //根据userid查询用户角色
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            List<UserRole> userRoleList = this.userRoleDao.select(userRole);
            if (userRoleList.size() > 0) {
                for (UserRole userRole1 : userRoleList) {
                    integerSet.add(userRole1.getRoleId());
                }
            }
            List<Integer> integerList = new ArrayList<>(integerSet);
            for (Integer i : integerList) {
                roleIds += i + ",";
            }
        }
        return roleIds;
    }


    /**
     * 根据企业id查询企业管理员
     *
     * @param companyId
     * @return 用户名
     */
    @Override
    public String selectByCompanyId(Integer companyId) {
        User u = new User();
        u.setCompanyId(companyId);
        u.setIsAdmin(3);
        User user = this.userDao.selectOne(u);
        if (user == null) {
            return null;
        }
        return user.getUsername();
    }


    /**
     * 修改用户的有效值
     *
     * @param companyId
     * @return
     */
    @Override
    @Transactional
    public boolean updateUserByCompanyId(Integer companyId) {
        User u = new User();
        u.setCompanyId(companyId);
        List<User> userList = this.userDao.select(u);
        for (User user : userList) {
            user.setId(user.getId());
            user.setEnable(0);
            this.userDao.updateByPrimaryKeySelective(user);
        }
        return true;
    }


    /**
     * app登录
     *
     * @param user
     * @param request
     * @param response
     * @return
     */
    @Transactional
    @Override
    public AppResult appLogin(User user, HttpServletRequest request, HttpServletResponse response) {
        user.setPassword(MD5Utils.md5(user.getPassword()));
        user.setIsAdmin(null);
        List<User> userList = this.userDao.select(user);
        if (userList != null && !userList.isEmpty()) {
            User loginUser = userList.get(0);
            if (loginUser.getEnable() == 0) {
                log.warn("用户已经被锁定不能登录，请与管理员联系！用户信息：{}", loginUser);
                return AppResult.msg("用户已经被锁定不能登录，请与管理员联系！");
            }
            // 设置最后登录时间
            user.setId(loginUser.getId());
            user.setLoginTime(new Date());
            this.userDao.updateByPrimaryKeySelective(user);
            // 获取用户信息
            String userJson = JSON.toJSONString(loginUser);
            // 获取权限数据
            List<Permission> permissionList = this.getPermission(loginUser.getId());
            String permissionJson = JSON.toJSONString(permissionList);
            // md5加密时间作为key，用户作为field，权限作为value。存入redis。格式为cookieValue：用户信息：用户权限。为HASH结构
            String key = MD5Utils.md5(System.currentTimeMillis() + loginUser.getUsername());
            this.redisService.hset(key, userJson, permissionJson, 14400);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("user", loginUser);
            // 将token返回
            resultMap.put(Constants.SMARTWF_TOKEN, key);
            return AppResult.data(resultMap);
        }
        log.warn("用户或密码不正确！");
        return AppResult.msg("用户或密码不正确！");

    }


    /**
     * 获取权限数据
     *
     * @param userId
     * @return
     */
    private List<Permission> getPermission(Integer userId) {
        // 拼接前端路由
        Set<Permission> permissionSet1 = this.permissionDao.loadUserPermissionByPatentId(userId, 0);
        List<Permission> permissionList1 = new ArrayList<>(permissionSet1);
        // 排序
        Collections.sort(permissionList1, (Permission o1, Permission o2) -> (o1.getId().compareTo(o2.getId())));
        List<Permission> permissions1 = new ArrayList<>();
        // 一级权限
        for (Permission permission : permissionList1) {
            if (permission.getParentId() == 0) {
                // 二级权限
                Set<Permission> permissionSet2 = this.permissionDao.loadUserPermissionByPatentId(userId, permission.getId());
                if (permissionSet2.isEmpty()) {
                    permissions1.add(permission);
                    continue;
                }
                List<Permission> permissionList2 = new ArrayList<>(permissionSet2);
                Collections.sort(permissionList2, (Permission o1, Permission o2) -> (o1.getId().compareTo(o2.getId())));
                permission.setChildren(permissionList2);

                List<Permission> permissions2 = new ArrayList<>();
                for (Permission permission2 : permissionList2) {
                    // 三级权限
                    Set<Permission> permissionSet3 = this.permissionDao.loadUserPermissionByPatentId(userId, permission2.getId());
                    if (permissionSet3.isEmpty()) {
                        permissions2.add(permission2);
                        continue;
                    }
                    List<Permission> permissionList3 = new ArrayList<>(permissionSet3);
                    Collections.sort(permissionList3, (Permission o1, Permission o2) -> (o1.getId().compareTo(o2.getId())));
                    permission2.setChildren(permissionList3);
                }
                // 去重，只有一级权限，没有二级权限、只有二级权限，没有三级权限
                permissionList2.removeAll(permissions2);
            }
        }
        // 去重，只有一级权限，没有二级权限、只有二级权限，没有三级权限
        permissionList1.removeAll(permissions1);
        return permissionList1;
    }
}
