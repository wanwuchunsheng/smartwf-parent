package com.smartwf.sso.config.rest;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @Auther: 赵明明
 * @Date: 2018/10/25 16:18
 * @Description: RestTemplate配置
 */
@SpringBootConfiguration
public class RestConfig {

    /**
     * 开启负载均衡
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
