package com.airshop.client;

import com.airshop.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author ouyanggang
 * @Date 2019/7/12 - 11:37
 */
@FeignClient(value = "item-service")
public interface CategoryClient extends CategoryApi {
}
