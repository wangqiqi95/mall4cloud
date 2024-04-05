package com.mall4j.cloud.api.crm.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 类描述：CRM 对接参数
 *
 * @date 2022/1/22 17:17：38
 */
@Data
@RefreshScope
@Component
public class CrmParamsConfig {

//	@Value("${crm.appkey:}")
//	private String appkey;

	/**
	 * 域名
	 */
	@Value("${crm.baseUrl:}")
	private String baseUrl;

	/**
	 * 接口验签(serviceName)
	 */
	@Value("${crm.callerService:callerService}")
	private String callerService;

	/**
	 * 接口验签(serviceSecret)
	 */
	@Value("${crm.serviceSecret:serviceSecret}")
	private String serviceSecret;

}
