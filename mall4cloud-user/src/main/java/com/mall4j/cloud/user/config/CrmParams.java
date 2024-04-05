package com.mall4j.cloud.user.config;

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
public class CrmParams {

	@Value("${crm.appkey:}")
	private String appkey;

	@Value("${crm.baseUrl:}")
	private String baseUrl;

	@Value("${crm.callerService:callerService}")
	private String callerService;

	@Value("${crm.serviceSecret:serviceSecret}")
	private String serviceSecret;

}
