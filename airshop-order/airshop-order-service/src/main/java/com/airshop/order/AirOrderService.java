package com.airshop.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author ouyanggang
 * @Date 2019/8/5 - 17:42
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.airshop")
@MapperScan(value = "com.airshop.order.mapper")
public class AirOrderService {

    public static void main(String[] args) {
        SpringApplication.run(AirOrderService.class, args);
    }
}
