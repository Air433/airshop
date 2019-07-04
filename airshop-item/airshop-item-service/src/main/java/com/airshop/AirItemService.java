package com.airshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author ouyanggang
 * @Date 2019/6/15 - 18:40
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.airshop.item.mapper")
public class AirItemService {

    public static void main(String[] args) {
        SpringApplication.run(AirItemService.class, args);
    }
}
