package com.mall4j.cloud.api.docking.skq_wm.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 类描述：微盟对接参数
 *
 * @author Peter_Tan
 * @date 2023/04/26
 */
//@RefreshScope
@ConfigurationProperties(prefix = "wm")
@Configuration
//@Component
@Data
public class DockingWmParams {

	/**
	 * 店铺授权码
	 */
	//@Value("${wm.code:}")
	private String code;

	/**
	 * OAuth 2.0 的授权类型
	 */
	//@Value("${wm.grantType:}")
	private String grantType;

	/**
	 * 产品应用的客户端 ID，是应用发起微盟 OAuth 2.0 授权的凭证
	 */
	//@Value("${wm.clientId:}")
	private String clientId;

	/**
	 * 产品应用的客户端密钥，是应用发起微盟 OAuth 2.0 授权的凭证
	 */
	//@Value("${wm.clientSecret:}")
	private String clientSecret;

	/**
	 * 应用发起请求时，所传的回调地址参数，用于接收平台返回的数据
	 */
	//@Value("${wm.redirectUri:}")
	private String redirectUri;

	/**
	 * 微盟接口域名/ip:port
	 */
	//@Value("${wm.url:}")
	private String url;

	/**
	 * token的超时时间,单位：秒
	 */
	//@Value("${wm.tokenExpire:60}")
	private long tokenExpire;

}
