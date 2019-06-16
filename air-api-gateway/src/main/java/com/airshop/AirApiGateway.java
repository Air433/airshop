package com.airshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @Author ouyanggang
 * @Date 2019/6/15 - 17:55
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class AirApiGateway {
    public static void main(String[] args) {
        SpringApplication.run(AirApiGateway.class, args);
    }
}
