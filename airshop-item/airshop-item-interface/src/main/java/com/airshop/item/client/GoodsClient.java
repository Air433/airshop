package com.airshop.item.client;

import com.airshop.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author ouyanggang
 * @Date 2019/7/12 - 11:33
 */
@FeignClient(value = "item-service")
public interface GoodsClient extends GoodsApi {
}
