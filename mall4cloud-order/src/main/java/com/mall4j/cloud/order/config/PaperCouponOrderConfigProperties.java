package com.mall4j.cloud.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "mall4cloud.paper")
@RefreshScope
public class PaperCouponOrderConfigProperties {
	
	private String webhookurl;
}
