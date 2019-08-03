package com.airshop.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author ouyanggang
 * @Date 2019/8/1 - 09:51
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.airshop")
public class AirCartApplication {
    public static void main(String[] args) {
        SpringApplication.run(AirCartApplication.class, args);
    }
}
