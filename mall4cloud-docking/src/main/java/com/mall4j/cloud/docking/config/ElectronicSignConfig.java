package com.mall4j.cloud.docking.config;

import com.mall4j.cloud.docking.client.ElectronicSignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述：阿里云电子加签对接配置
 */
@Configuration
public class ElectronicSignConfig {

	@Value("${electronic.sign.regionId:}")
	private String regionId;

	@Value("${electronic.sign.accessKeyId:}")
	private String accessKeyId;

	@Value("${electronic.sign.secret:}")
	private String secret;

	@Value("${electronic.sign.storeId:}")
	private String storeId;

	@Bean
	public ElectronicSignClient electronicSignClient() {
		ElectronicSignClient electronicSignClient = new ElectronicSignClient(regionId, accessKeyId, secret);
		electronicSignClient.setDefaultStorId(storeId);
		return electronicSignClient;
	}
}
