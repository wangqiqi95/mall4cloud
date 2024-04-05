package com.mall4j.cloud.biz.config;

import com.mall4j.cloud.biz.model.cp.Config;
import com.mall4j.cloud.biz.service.cp.ConfigService;
import com.mall4j.cloud.common.util.Json;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.util.http.apache.DefaultApacheHttpClientBuilder;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.impl.WxCpRedissonConfigImpl;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Administrator
 * @Description:
 * @Date: 2022-04-01 14:24
 * @Version: 1.0
 */
@Slf4j
@Configuration
public class WxCpMiniProgramConfig {
    private static final String CP_MIN_KEY_PREFIX = "mall4cloud_biz:cp_min_key_prefix";
    private   ConfigService configService;
    private   RedissonClient redissonClient;
    public static String MINI_PROGRAM_APP_ID;
    @Autowired
    public WxCpMiniProgramConfig(ConfigService configService, RedissonClient redissonClient) {
        this.configService = configService;
        this.redissonClient = redissonClient;
    }
    @Bean
    public WxCpService getWxCpOAuthService() {
        Config config = configService.getConfig();
        MINI_PROGRAM_APP_ID = config.getMinAppId();
        WxCpService wxConnectCpService = new WxCpServiceImpl();
        if(config.getAgentId()!=null) {
            WxCpRedissonConfigImpl wxCpRedisConfig = new WxCpRedissonConfigImpl(redissonClient, CP_MIN_KEY_PREFIX);
            wxCpRedisConfig.setCorpId(config.getCpId());
            wxCpRedisConfig.setAgentId(config.getAgentId());
            wxCpRedisConfig.setCorpSecret(config.getAgentSecret());

            DefaultApacheHttpClientBuilder apacheHttpClientBuilder = DefaultApacheHttpClientBuilder.get();
            apacheHttpClientBuilder.setConnectionTimeout(20000);
            apacheHttpClientBuilder.setSoTimeout(20000);
            wxCpRedisConfig.setApacheHttpClientBuilder(apacheHttpClientBuilder);

            wxConnectCpService.setWxCpConfigStorage(wxCpRedisConfig);
        }
        return   wxConnectCpService;
    }
}
