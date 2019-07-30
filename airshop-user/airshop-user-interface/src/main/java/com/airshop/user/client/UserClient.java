package com.airshop.user.client;

import com.airshop.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author ouyanggang
 * @Date 2019/7/29 - 15:54
 */
@FeignClient(value = "user-service")
public interface UserClient extends UserApi {
}
