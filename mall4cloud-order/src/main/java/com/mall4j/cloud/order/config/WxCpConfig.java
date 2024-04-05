package com.mall4j.cloud.order.config;

import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.WxCpConfigStorage;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author axin
 * @Date 2022-09-08 18:13
 **/
@Configuration
public class WxCpConfig {
    @Bean
    public WxCpService getWxCpOAuthService() {
        WxCpService wxConnectCpService = new WxCpServiceImpl();
        WxCpConfigStorage wxCpConfigStorage = new WxCpDefaultConfigImpl();
        wxConnectCpService.setWxCpConfigStorage(wxCpConfigStorage);
        return wxConnectCpService;
    }
}
