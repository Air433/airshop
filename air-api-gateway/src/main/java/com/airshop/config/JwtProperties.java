package com.airshop.config;

import com.airshop.auth.utils.RsaUtils;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.RSAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.security.PublicKey;

/**
 * @Author ouyanggang
 * @Date 2019/7/30 - 15:43
 */
@ConfigurationProperties(prefix = "airshop.jwt")
@Component
@RefreshScope
public class JwtProperties {

    private PublicKey publicKey;

    @Value("${airshop.jwt.pubKeyPath}")
    private String pubKeyPath;

    @Value("${airshop.jwt.cookieName}")
    private String cookieName;

    private static final Logger log = LoggerFactory.getLogger(JwtProperties.class);

    @PostConstruct
    public void init(){
        try {
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            log.error("初始化公钥失败！", e);
            throw new RuntimeException();
        }
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }
}
