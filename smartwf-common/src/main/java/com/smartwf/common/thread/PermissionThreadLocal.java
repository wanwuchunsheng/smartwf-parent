package com.smartwf.common.thread;

/**
 * @Auther: WCH
 * @Date: 2018/11/5 10:10
 * @Description: 用户权限本地线程
 */
public class PermissionThreadLocal {


    // 本地线程，存放登录用户权限
    private static ThreadLocal<String> permissionThreadLocal = new ThreadLocal<>();


    /**
     * 储存用户权限
     * @param permission
     */
    public static void setPermission(String permission) {
        permissionThreadLocal.set(permission);
    }


    /**
     * 获取用户权限
     */
    public static String getPermission() {
        return permissionThreadLocal.get();
    }

}
