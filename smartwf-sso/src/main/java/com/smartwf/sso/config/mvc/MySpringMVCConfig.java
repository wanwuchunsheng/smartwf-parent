package com.smartwf.sso.config.mvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.smartwf.sso.interceptor.LoginInterceptor;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: 赵明明
 * @Date: 2018/11/2 10:13
 * @Description: springMVC配置
 */
@SpringBootConfiguration
@Slf4j
public class MySpringMVCConfig implements WebMvcConfigurer {


    @Autowired
    private LoginInterceptor loginInterceptor;


    /**
     * 添加自定义拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/app/login")
                .excludePathPatterns("/logout")
                .excludePathPatterns("/druid/**")
                .excludePathPatterns("/user/selectByUsername")
                // swagger2页面
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/error/")
                // rpc注册中心
                .excludePathPatterns("/rpc/**");
    }


    /**
     * 请求参数时间格式化
     *
     * @return
     */
    @Bean
    public Converter<String, Date> addNewConvert() {

        return new Converter<String, Date>() {

            @Override
            public Date convert(String source) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = null;
                try {
                    if (StringUtils.isNotBlank(source)) {
                        date = sdf.parse(source);
                    }
                } catch (ParseException e) {
                    log.error("字符串转时间格式化错误：{}", e.getMessage(), e);
                }
                return date;
            }
        };
    }

}
