package com.mall4j.cloud.distribution.config;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceHttpClientImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedissonConfigImpl;
import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.common.bean.WxMiniApp;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;


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

    private static final String MA_CONFIG_KEY_PREFIX = "mall4cloud:ma_config_key_prefix";

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


}
