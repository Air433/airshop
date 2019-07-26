package com.airshop.user.api;

import com.airshop.user.pojo.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author ouyanggang
 * @Date 2019/7/26 - 14:16
 */
public interface UserApi {

    /**
     * 用户验证
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    ResponseEntity<User> queryUser(@RequestParam("username") String username, @RequestParam("password") String password);
}
