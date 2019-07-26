package com.airshop.user.controller;

import com.airshop.user.api.UserApi;
import com.airshop.user.pojo.User;
import com.airshop.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @Author ouyanggang
 * @Date 2019/7/26 - 14:20
 */
@Controller
public class UserController implements UserApi {

    @Autowired
    private UserService userService;

    /**
     * 用户数据检查
     * @param data
     * @param type
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserData(@PathVariable("data") String data, @PathVariable("type")Integer type){
        Boolean result = this.userService.checkData(data, type);

        return Optional.of(result).map(r->ResponseEntity.ok(r)).
                orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("code")
    public ResponseEntity senVertifyCode(@RequestParam("phone") String phone){
        Boolean result = this.userService.sendVertifyCode(phone);
        if (result == null || !result){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code")String code){
        Boolean result = this.userService.register(user, code);

        if (result == null || !result){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 用户验证
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    @Override
    public ResponseEntity<User> queryUser(String username, String password) {
        User user = this.userService.queryUser(username, password);
        return ResponseEntity.ok(user);
    }
}
