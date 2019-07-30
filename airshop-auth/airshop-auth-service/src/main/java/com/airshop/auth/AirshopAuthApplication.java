package com.airshop.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author ouyanggang
 * @Date 2019/7/29 - 11:57
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.airshop")
public class AirshopAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirshopAuthApplication.class, args);
    }
}
