package com.airshop.order.config;

import com.airshop.interceptor.LoginInterceptor;
import com.airshop.properties.JwtProp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author ouyanggang
 * @Date 2019/8/1 - 10:59
 */
@Configuration
@EnableConfigurationProperties(value = JwtProp.class)
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtProp jwtProp;

    @Bean
    public LoginInterceptor loginInterceptor(){
        return new LoginInterceptor(jwtProp);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/**");
    }
}
