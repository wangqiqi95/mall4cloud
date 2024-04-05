package com.mall4j.cloud.docking.config;

import com.mall4j.cloud.api.docking.jos.dto.JosIntefaceContext;
import com.mall4j.cloud.docking.utils.JosClients;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
public class JosConfig implements InitializingBean {

	@Value("${jos.url:https://api.jd.com/routerjson}")
	private String url;

	@Value("${jos.appkey:}")
	private String appKey;

	@Value("${jos.appSecret:}")
	private String appSecret;

	/**
	 * 平台编号,益世分配，例如：“XXXX”
	 */
	@Value("${jos.platformCode:}")
	private String platformCode;

	/**
	 * 业务规则编号,益世分配，例如：“1111”
	 */
	@Value("${jos.ruleNo:}")
	private String ruleNo;

	/**
	 * 联营分佣业务规则编号,益世分配，例如：“1111”
	 */
	@Value("${jos.jointVentureRuleNo:}")
	private String jointVentureRuleNo;

	@Override
	public void afterPropertiesSet() throws Exception {
		JosIntefaceContext context = new JosIntefaceContext();
		context.setPlatformCode(this.getPlatformCode());
		context.setRuleNo(this.getRuleNo());
		context.setJointVentureRuleNo(this.getJointVentureRuleNo());
		JosClients.clients().init(this.getUrl(), this.getAppKey(), this.getAppSecret(), context);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	public String getRuleNo() {
		return ruleNo;
	}

	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}

	public String getJointVentureRuleNo() {
		return jointVentureRuleNo;
	}

	public void setJointVentureRuleNo(String jointVentureRuleNo) {
		this.jointVentureRuleNo = jointVentureRuleNo;
	}
}
