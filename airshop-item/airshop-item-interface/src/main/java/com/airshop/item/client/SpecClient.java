package com.airshop.item.client;

import com.airshop.item.api.SpecApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author ouyanggang
 * @Date 2019/7/15 - 20:59
 */
@FeignClient(value = "item-service")
public interface SpecClient extends SpecApi {
}
