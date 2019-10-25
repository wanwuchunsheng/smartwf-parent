package com.smartwf.common.utils;


import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.smartwf.common.pojo.BasePojo;

/**
 * @Auther:
 * @Description: 通用工具类
 */
public class CommonUtils {


    /**
     * 设置创建人、修改时间、修改人、修改时间
     * @param basePojo
     * @param StringName
     */
    public static void setUserAndTime(BasePojo basePojo, String StringName) {
        basePojo.setCreateTime(new Date());
        basePojo.setCreateUser(StringName);
        basePojo.setUpdateTime(new Date());
        basePojo.setUpdateUser(StringName);
    }


    /**
     * 设置修改人跟修改时间
     * @param basePojo
     * @param StringName
     */
    public static void setUpdateUserAndUpdateTime(BasePojo basePojo, String StringName) {
        basePojo.setUpdateTime(new Date());
        basePojo.setUpdateUser(StringName);
    }


    /**
     * 获取文件后缀
     * @param fileName 文件
     * @return
     */
    public static String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."),fileName.length());
    }


    /**
     * 获取7位流水号
     * @return
     */
    public static String getSerialNumber() {
        String s = String.valueOf(System.nanoTime());
        String nanoTime = StringUtils.substring(s, s.length() - 7, s.length());
        return nanoTime;
    }


    /**
     * 获取12位流水号
     * @return
     */
    public static String getNumber() {
        String s = String.valueOf(System.nanoTime());
        String nanoTime = StringUtils.substring(s, s.length() - 12, s.length());
        return nanoTime;
    }

    /**
     * 获取4位随机数
     * @return
     */
    public static String getRandom() {
        String s = String.valueOf(System.nanoTime());
        String nanoTime = StringUtils.substring(s, s.length() - 4, s.length());
        return nanoTime;
    }


    /**
     * 获取3位随机数
     * @return
     */
    public static String getRandom3() {
        String s = String.valueOf(System.nanoTime());
        String nanoTime = StringUtils.substring(s, s.length() - 3, s.length());
        return nanoTime;
    }


    /**
     * 获取真实ip
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if( ip.indexOf(",")!=-1 ){
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return StringUtils.equalsIgnoreCase(ip, "0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }


    /**
     * 获取文件名称
     * @return key
     */
    public static String getKey() {
        String timestamp = DateUtils.getTimestamp();
        String s = UUID.randomUUID().toString();
        // delete "-"
        String uuid = s.replace("-", "");
        return timestamp + uuid;
    }

}
