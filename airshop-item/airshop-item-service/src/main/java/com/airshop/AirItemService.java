package com.airshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author ouyanggang
 * @Date 2019/6/15 - 18:40
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AirItemService {
    public static void main(String[] args) {
        SpringApplication.run(AirItemService.class, args);
    }
}
