package com.mall4j.cloud.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Configuration
@ConfigurationProperties(prefix = "mall4cloud.product")
@RefreshScope
public class ProductConfigProperties {
	private Integer exportSpupageSize;
	private Integer soldskupricelogpagesize;
	private Integer priceSyncFlag;
	private Integer proSyncFlag;
	private Integer stockSyncFlag;
	private Boolean saveSkuPricELog;
	private Boolean savePushPSLog;
	private Integer redisCacheSpu;
	private Boolean redisCacheSpuFlag;

	/**
	 * 有数同步开关
	 */
	private Boolean syncZhlsData=true;
}
