package com.mall4j.cloud.platform.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义配置项
 */
@RefreshScope
@Configuration
@Data
public class CustomConfig {

    @Value("custom.micro-page.prefix")
    private String microPagePrefix;

}
