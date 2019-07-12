package com.airshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author ouyanggang
 * @Date 2019/7/12 - 09:49
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class AirSearchService {
    public static void main(String[] args) {
        SpringApplication.run(AirSearchService.class,args);
    }
}
