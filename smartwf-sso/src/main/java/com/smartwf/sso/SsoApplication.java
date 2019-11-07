package com.smartwf.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 启动类
 */
@SpringCloudApplication
@EnableFeignClients // 启动feign
@EnableHystrixDashboard // 启用Hystrix Dashboard功能
@MapperScan(basePackages = {"com.smartwf.sso.dao"})
@ComponentScan(basePackages = {"com.smartwf"})
public class SsoApplication {

   public static void main(String[] args) {
      SpringApplication.run(SsoApplication.class, args);
   }
}