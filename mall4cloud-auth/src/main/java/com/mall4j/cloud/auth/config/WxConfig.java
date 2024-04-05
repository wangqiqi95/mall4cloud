package com.mall4j.cloud.auth.config;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceHttpClientImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedissonConfigImpl;
import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.common.bean.WxMiniApp;
import com.mall4j.cloud.common.bean.WxMp;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceHttpClientImpl;
import me.chanjar.weixin.mp.config.WxMpHostConfig;
import me.chanjar.weixin.mp.config.impl.WxMpRedissonConfigImpl;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 通过微信配置获取微信的支付信息，登陆信息等
 * @author FrozenWatermelon
 */
@RefreshScope
@Component
public class WxConfig {


    @Autowired
    private FeignShopConfig feignShopConfig;

    @Autowired
    private RedissonClient redissonClient;

    @Value("${proxy.weixin.api:}")
    private String proxyUrl;


    private static final String MA_CONFIG_KEY_PREFIX = "mall4cloud:ma_config_key_prefix";

    private static final String MP_CONFIG_KEY_PREFIX = "mall4cloud:mp_config_key_prefix";

    public WxMaService getWxMaService() {
        WxMiniApp wxMiniApp = feignShopConfig.getWxMiniApp();
        // access token用redis缓存
        WxMaRedissonConfigImpl maRedissonConfig = new WxMaRedissonConfigImpl(redissonClient, MA_CONFIG_KEY_PREFIX);
        maRedissonConfig.setAppid(wxMiniApp.getAppId());
        maRedissonConfig.setSecret(wxMiniApp.getSecret());

        WxMaServiceHttpClientImpl wxMaServiceHttpClient = new WxMaServiceHttpClientImpl();
        wxMaServiceHttpClient.setWxMaConfig(maRedissonConfig);
        return wxMaServiceHttpClient;
    }

    public WxMpService getWxMpService() {
        WxMp wxMp = feignShopConfig.getWxMp();
        // access token用redis缓存
        WxMpRedissonConfigImpl wxMpRedisConfig = new WxMpRedissonConfigImpl(redissonClient, MP_CONFIG_KEY_PREFIX);
        wxMpRedisConfig.setAppId(wxMp.getAppId());
        wxMpRedisConfig.setSecret(wxMp.getSecret());

        if (Objects.nonNull(proxyUrl) && !proxyUrl.isEmpty()) {
            WxMpHostConfig hostConfig = new WxMpHostConfig();
            hostConfig.setApiHost(proxyUrl);
            wxMpRedisConfig.setHostConfig(hostConfig);
        }

        WxMpServiceHttpClientImpl wxMpServiceHttpClient = new WxMpServiceHttpClientImpl();
        wxMpServiceHttpClient.setWxMpConfigStorage(wxMpRedisConfig);
        return wxMpServiceHttpClient;
    }

}
