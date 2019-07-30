package com.airshop.filter;

import com.airshop.auth.utils.JwtUtils;
import com.airshop.config.FilterProperties;
import com.airshop.config.JwtProperties;
import com.airshop.utils.CookieUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @Author ouyanggang
 * @Date 2019/7/30 - 16:19
 */
@Component
public class LoginFilter extends ZuulFilter {
    @Autowired
    private JwtProperties properties;
    @Autowired
    private FilterProperties filterProperties;

    private static final Logger log = LoggerFactory.getLogger(LoginFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String requestUri = request.getRequestURI();
        log.info(requestUri);

        return !isAllowPath(requestUri);
    }

    private boolean isAllowPath(String requestUri) {
        boolean flag = false;
        List<String> paths = Arrays.asList(this.filterProperties.getAllowPaths().split(" "));
        for (String path : paths) {
            if (requestUri.startsWith(path)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();

        HttpServletRequest request = context.getRequest();

        String token = CookieUtils.getCookieValue(request, this.properties.getCookieName());

        try {
            JwtUtils.getInfoFromToken(token, this.properties.getPublicKey());
        } catch (Exception e) {
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
            log.error("非法访问，未登录，地址：{}", request.getRemoteHost(), e );
        }

        return null;
    }
}
