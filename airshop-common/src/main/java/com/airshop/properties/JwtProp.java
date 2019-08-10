package com.airshop.properties;

import com.airshop.auth.utils.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * @Author ouyanggang
 * @Date 2019/8/1 - 09:57
 */
@Component
@ConfigurationProperties(prefix = "airshop.jwt")
@RefreshScope
public class JwtProp {

    private String pubKeyPath;

    private PublicKey publicKey;

    private String cookieName;

    private static final Logger log = LoggerFactory.getLogger(JwtProp.class);

    @PostConstruct
    public void init(){
        try {
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            log.error("初始化公钥失败！", e);
            throw new RuntimeException();
        }
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }
}
