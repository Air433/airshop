package com.airshop.user.service;

import com.airshop.user.pojo.User;

/**
 * @Author ouyanggang
 * @Date 2019/7/26 - 14:23
 */
public interface UserService {

    /**
     * 检查用户名和手机号是否可用
     * @param data
     * @param type
     * @return
     */
    Boolean checkData(String data, Integer type);

    Boolean sendVertifyCode(String phone);

    User queryUser(String username, String password);

    Boolean register(User user, String code);
}
