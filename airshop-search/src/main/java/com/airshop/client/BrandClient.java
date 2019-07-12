package com.airshop.client;

import com.airshop.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author ouyanggang
 * @Date 2019/7/12 - 13:09
 */
@FeignClient(value = "item-service")
public interface BrandClient extends BrandApi {
}
