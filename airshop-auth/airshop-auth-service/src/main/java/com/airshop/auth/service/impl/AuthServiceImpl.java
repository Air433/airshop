package com.airshop.auth.service.impl;

import com.airshop.auth.entity.UserInfo;
import com.airshop.auth.properties.JwtProperties;
import com.airshop.auth.service.AuthService;
import com.airshop.auth.utils.JwtUtils;
import com.airshop.user.client.UserClient;
import com.airshop.user.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author ouyanggang
 * @Date 2019/7/29 - 15:42
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtProperties properties;
    @Override
    public String authentication(String username, String password) {

        try{
            User user = this.userClient.queryUser(username, password).getBody();
            if (user ==null){
                return null;
            }
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()),
                    properties.getPrivateKey(), properties.getExpire());
            return token;
        }catch (Exception e){
            log.error("授权失败！", e);
            return null;
        }

    }
}
