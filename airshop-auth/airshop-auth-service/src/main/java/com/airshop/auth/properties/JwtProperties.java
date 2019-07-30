package com.airshop.auth.properties;

import com.airshop.auth.utils.RsaUtils;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.RSAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Author ouyanggang
 * @Date 2019/7/29 - 14:21
 */
@Configuration
@RefreshScope
public class JwtProperties {

    private static final Logger log = LoggerFactory.getLogger(JwtProperties.class);
    /**
     * 密钥
     */
    @Value("${airshop.jwt.secret}")
    private String secret;

    /**
     * 公钥地址
     */
    @Value("${airshop.jwt.pubKeyPath}")
    private String pubKeyPath;

    /**
     * 私钥地址
     */
    @Value("${airshop.jwt.priKeyPath}")
    private String priKeyPath;

    /**
     * token过期时间
     */
    @Value("${airshop.jwt.expire}")
    private int expire;

    /**
     * 公钥
     */
    private PublicKey publicKey;

    /**
     * 私钥
     */
    private PrivateKey privateKey;

    @Value("${airshop.jwt.cookieName}")
    private String cookieName;

    @Value("${airshop.jwt.cookieMaxAge}")
    private Integer cookieMaxAge;

    @PostConstruct
    public void init(){
        try{
            File pubKey = new File(pubKeyPath);
            File priKey = new File(priKeyPath);

            if (!pubKey.exists() || !priKey.exists()){
                RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
            }
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
            this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
        }catch (Exception e){
            log.error("初始化公钥和私钥失败！", e);
            throw new RuntimeException();
        }
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public Integer getCookieMaxAge() {
        return cookieMaxAge;
    }

    public void setCookieMaxAge(Integer cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getPriKeyPath() {
        return priKeyPath;
    }

    public void setPriKeyPath(String priKeyPath) {
        this.priKeyPath = priKeyPath;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}
