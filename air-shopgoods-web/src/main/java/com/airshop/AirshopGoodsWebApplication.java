package com.airshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author ouyanggang
 * @Date 2019/7/16 - 14:17
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class AirshopGoodsWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(AirshopGoodsWebApplication.class);
    }
}
