package com.mall4j.cloud.openapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 类描述：CRM 对接参数
 *
 * @date 2022/1/22 17:17：38
 */
@RefreshScope
@Component
public class CrmParams {

	@Value("${crm.appkey}")
	private String appkey;

	@Value("${crm.appsecret}")
	private String appsecret;

	@Value("${crm.baseUrl}")
	private String baseUrl;

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getAppsecret() {
		return appsecret;
	}

	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
}
