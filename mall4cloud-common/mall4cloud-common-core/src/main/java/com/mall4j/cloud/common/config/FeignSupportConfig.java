package com.mall4j.cloud.common.config;

import com.mall4j.cloud.common.feign.FeignBasicAuthRequestInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign配置注册（全局）
 *
 * @author YXF
 * @date 2021/05/19
 */
@Configuration
public class FeignSupportConfig {

    @Bean
    public RequestInterceptor requestInterceptor(){
        return new FeignBasicAuthRequestInterceptor();
    }
}
