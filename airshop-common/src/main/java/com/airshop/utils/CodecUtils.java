package com.airshop.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Author ouyanggang
 * @Date 2019/7/26 - 17:41
 */
public class CodecUtils {

    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String passwordBcryptEncode(String username, String password){
        return encoder.encode(username + password);
    }

    public static Boolean passwordConfirm(String rawPassword,String encodePassword){
        return encoder.matches(rawPassword, encodePassword);
    }
}
