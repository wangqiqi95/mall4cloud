package com.mall4j.cloud.openapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
public class IphParams {

    /**
     * 爱铺货 appkey
     */
    @Value("${auth.token.iph.appkey}")
    public String iphAppkey;

    /**
     * 爱铺货 appsecret
     */
    @Value("${auth.token.iph.appsecret}")
    public String iphAppsecret;

    /**
     * 是否后台跳转
     */
    @Value("${auth.token.iph.redirect:false}")
    public boolean redirect;

    public String getIphAppkey() {
        return iphAppkey;
    }

    public void setIphAppkey(String iphAppkey) {
        this.iphAppkey = iphAppkey;
    }

    public String getIphAppsecret() {
        return iphAppsecret;
    }

    public void setIphAppsecret(String iphAppsecret) {
        this.iphAppsecret = iphAppsecret;
    }

    public boolean isRedirect() {
        return redirect;
    }

    public void setRedirect(boolean redirect) {
        this.redirect = redirect;
    }
}
