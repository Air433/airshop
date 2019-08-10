package com.airshop.config;

import com.airshop.properties.IdWorkerProperties;
import com.airshop.utils.IdWorker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author ouyanggang
 * @Date 2019/8/5 - 15:43
 */
@EnableConfigurationProperties(IdWorkerProperties.class)
public class IdWorkerConfig {

    @Bean
   public IdWorker idWorker(IdWorkerProperties prop){
       return new IdWorker(prop.getWorkerId(), prop.getDataCenterId());
   }
}
