package com.airshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author ouyanggang
 * @Date 2019/7/5 - 11:19
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AirUploadService {
    public static void main(String[] args) {
        SpringApplication.run(AirUploadService.class, args);
    }
}
