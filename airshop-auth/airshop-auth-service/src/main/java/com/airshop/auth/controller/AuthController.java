package com.airshop.auth.controller;

import com.airshop.auth.entity.UserInfo;
import com.airshop.auth.properties.JwtProperties;
import com.airshop.auth.service.AuthService;
import com.airshop.auth.utils.JwtUtils;
import com.airshop.utils.CookieUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author ouyanggang
 * @Date 2019/7/29 - 15:41
 */
@Controller
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("accredit")
    public ResponseEntity<Void> authentication(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        String token = this.authService.authentication(username, password);
        if (StringUtils.isBlank(token)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), token, jwtProperties.getCookieMaxAge(), true);

        return ResponseEntity.ok().build();
    }

    @GetMapping("verify")
    public ResponseEntity<UserInfo> verifyUser(@CookieValue("AIR_TOKEN")String token, HttpServletRequest request,
                                               HttpServletResponse response){
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());
            token = JwtUtils.generateToken(userInfo, this.jwtProperties.getPrivateKey(),
                    this.jwtProperties.getExpire());
            CookieUtils.setCookie(request, response, this.jwtProperties.getCookieName(), token, this.jwtProperties.getCookieMaxAge());
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
