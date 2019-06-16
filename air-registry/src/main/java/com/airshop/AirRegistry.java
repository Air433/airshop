package com.airshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Author ouyanggang
 * @Date 2019/6/15 - 17:26
 */
@SpringBootApplication
@EnableEurekaServer
public class AirRegistry {
    public static void main(String[] args) {
        SpringApplication.run(AirRegistry.class, args);
    }
}
