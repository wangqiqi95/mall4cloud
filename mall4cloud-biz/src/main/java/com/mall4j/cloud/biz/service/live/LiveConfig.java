package com.mall4j.cloud.biz.service.live;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@RefreshScope
@Configuration
@Getter
@Setter
public class LiveConfig {

    @Value("${biz.oss.resources-url}")
    private String imgDomain = "";

}