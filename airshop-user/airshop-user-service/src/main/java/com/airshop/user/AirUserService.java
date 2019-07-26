package com.airshop.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author ouyanggang
 * @Date 2019/7/26 - 16:16
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.airshop.user.mapper")
public class AirUserService {

    public static void main(String[] args) {
        SpringApplication.run(AirUserService.class, args);
    }
}
