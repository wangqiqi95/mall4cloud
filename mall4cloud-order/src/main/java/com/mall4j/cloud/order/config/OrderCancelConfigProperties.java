package com.mall4j.cloud.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "mall4cloud.order.cancel.delay")
@RefreshScope
public class OrderCancelConfigProperties {
	//  #延迟消息 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h (1-18)
	private int level;
	
	//视频号4.0订单过期时间
	private int channelsTime;
	
	private final static int[] DELAY_LEVEL = {0, 1000, 5000, 10000, 30000, 60000, 120000,
			180000, 240000, 300000, 360000, 420000, 480000, 540000, 600000, 1200000, 1800000, 3600000, 7200000};
	
	public int fetchDelayTime() {
		return DELAY_LEVEL[level];
	}
}
