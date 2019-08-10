package com.airshop.order.config;

import com.github.wxpay.sdk.WXPayConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * @Author ouyanggang
 * @Date 2019/8/10 - 15:00
 */
@ConfigurationProperties(prefix = "airshop.pay")
@Configuration
public class PayConfig implements WXPayConfig {

    private String appID;

    private String mchID;

    private String key;

    private int httpConnectTimeoutMs;

    private int httpReadTimeoutMs;

    private String notifyUrl;

    @Override
    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }
    @Override
    public String getMchID() {
        return mchID;
    }

    public void setMchID(String mchID) {
        this.mchID = mchID;
    }
    @Override
    public String getKey() {
        return key;
    }

    @Override
    public InputStream getCertStream() {
        return null;
    }

    public void setKey(String key) {
        this.key = key;
    }
    @Override
    public int getHttpConnectTimeoutMs() {
        return httpConnectTimeoutMs;
    }

    public void setHttpConnectTimeoutMs(int httpConnectTimeoutMs) {
        this.httpConnectTimeoutMs = httpConnectTimeoutMs;
    }
    @Override
    public int getHttpReadTimeoutMs() {
        return httpReadTimeoutMs;
    }

    public void setHttpReadTimeoutMs(int httpReadTimeoutMs) {
        this.httpReadTimeoutMs = httpReadTimeoutMs;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}
