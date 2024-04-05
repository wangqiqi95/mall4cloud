package com.mall4j.cloud.order.config;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceHttpClientImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedissonConfigImpl;
import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.common.bean.WxMiniApp;
import com.mall4j.cloud.common.bean.WxMp;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.http.apache.DefaultApacheHttpClientBuilder;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceHttpClientImpl;
import me.chanjar.weixin.mp.config.impl.WxMpRedissonConfigImpl;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 通过微信配置获取微信的支付信息，登陆信息等
 * @author FrozenWatermelon
 */
@Component
@Slf4j
public class WxConfig {


    @Autowired
    private FeignShopConfig feignShopConfig;

    @Autowired
    private RedissonClient redissonClient;



    private static final String MA_CONFIG_KEY_PREFIX = "mall4cloud:ma_config_key_prefix";

    private static final String MP_CONFIG_KEY_PREFIX = "mall4cloud:mp_config_key_prefix";

    private static final String CP_CONFIG_KEY_PREFIX = "mall4cloud:cp_config_key_prefix";

    public WxMaService getWxMaService() {
        WxMiniApp wxMiniApp = feignShopConfig.getWxMiniApp();
        // access token用redis缓存
        WxMaRedissonConfigImpl maRedissonConfig = new WxMaRedissonConfigImpl(redissonClient, MA_CONFIG_KEY_PREFIX);
        maRedissonConfig.setAppid(wxMiniApp.getAppId());
        maRedissonConfig.setSecret(wxMiniApp.getSecret());

        DefaultApacheHttpClientBuilder apacheHttpClientBuilder = DefaultApacheHttpClientBuilder.get();
        apacheHttpClientBuilder.setConnectionTimeout(20000);
        apacheHttpClientBuilder.setSoTimeout(20000);
        maRedissonConfig.setApacheHttpClientBuilder(apacheHttpClientBuilder);

        WxMaServiceHttpClientImpl wxMaServiceHttpClient = new WxMaServiceHttpClientImpl();
        wxMaServiceHttpClient.setWxMaConfig(maRedissonConfig);
        //log.info("wxMaServiceHttpClient = {}", JSON.toJSONString(wxMaServiceHttpClient.getRequestHttpClient()));
        return wxMaServiceHttpClient;
    }

    public String getWxMaToken() {
        try {
            return this.getWxMaService().getAccessToken();
        } catch (WxErrorException e) {
            log.error("获取微信token失败", e);
        }
        return "";
    }

    public WxMpService getWxMpService() {
        WxMp wxMp = feignShopConfig.getWxMp();
        // access token用redis缓存
        WxMpRedissonConfigImpl wxMpRedisConfig = new WxMpRedissonConfigImpl(redissonClient, MP_CONFIG_KEY_PREFIX);
        wxMpRedisConfig.setAppId(wxMp.getAppId());
        wxMpRedisConfig.setSecret(wxMp.getSecret());

        WxMpServiceHttpClientImpl wxMpServiceHttpClient = new WxMpServiceHttpClientImpl();
        wxMpServiceHttpClient.setWxMpConfigStorage(wxMpRedisConfig);
        return wxMpServiceHttpClient;
    }

    public WxMpService getWxMpService(WxMp wxMp) {
        // access token用redis缓存
        WxMpRedissonConfigImpl wxMpRedisConfig = new WxMpRedissonConfigImpl(redissonClient, MP_CONFIG_KEY_PREFIX);
        wxMpRedisConfig.setAppId(wxMp.getAppId());
        wxMpRedisConfig.setSecret(wxMp.getSecret());

        WxMpServiceHttpClientImpl wxMpServiceHttpClient = new WxMpServiceHttpClientImpl();
        wxMpServiceHttpClient.setWxMpConfigStorage(wxMpRedisConfig);
        return wxMpServiceHttpClient;
    }

}
