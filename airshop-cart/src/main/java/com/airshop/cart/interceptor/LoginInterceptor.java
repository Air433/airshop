package com.airshop.cart.interceptor;

import com.airshop.auth.entity.UserInfo;
import com.airshop.auth.utils.JwtUtils;
import com.airshop.cart.config.JwtProperties;
import com.airshop.utils.CookieUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author ouyanggang
 * @Date 2019/8/1 - 10:21
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private JwtProperties properties;

    private static final ThreadLocal<UserInfo> tl = new ThreadLocal<>();

    public LoginInterceptor(JwtProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = CookieUtils.getCookieValue(request, "AIR_TOKEN");
        if (StringUtils.isBlank(token)){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        try {
            UserInfo user = JwtUtils.getInfoFromToken(token, properties.getPublicKey());
            tl.set(user);
            return true;
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        tl.remove();
    }

    public static UserInfo getLoginUser(){
        return tl.get();
    }
}
