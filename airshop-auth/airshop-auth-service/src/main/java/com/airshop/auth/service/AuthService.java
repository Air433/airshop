package com.airshop.auth.service;

/**
 * @Author ouyanggang
 * @Date 2019/7/29 - 15:42
 */
public interface AuthService {

    /**
     * 用户授权
     * @param username
     * @param password
     * @return
     */
    String authentication(String username, String password);
}
