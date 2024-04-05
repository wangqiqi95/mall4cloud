package com.mall4j.cloud.common.bean;

/**
 * @author eury_guo
 */
public class WxOpen {
    /**
     * 设置微信公众号的appid
     */
    private String appId;

    /**
     * 设置微信公众号的Secret
     */
    private String secret;

    /**
     * 微信公众号消息加解密token
     */
    private String token;

    /**
     * 微信公众号消息加解密aesKey
     */
    private String aesKey;

    /**
     * 授权回调页
     */
    private String redirectUri;

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }
}
