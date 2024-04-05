package com.mall4j.cloud.openapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "qiyukf")
public class QiyukfConfigProperties {

    private String appId;

    private String appSecret;

    private String signKey;

    private Long expiresIn;

    private String url;


}
