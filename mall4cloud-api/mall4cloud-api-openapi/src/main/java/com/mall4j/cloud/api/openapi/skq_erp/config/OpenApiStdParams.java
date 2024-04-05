package com.mall4j.cloud.api.openapi.skq_erp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 类描述：中台对接参数
 *
 * @date 2022/1/7 1:19：14
 */
@RefreshScope
@Component
public class OpenApiStdParams {

	@Value("${std.userName}")
	private String userName;

	@Value("${std.userKey}")
	private String userKey;

	@Value("${std.isTest:false}")
	private Boolean isTest;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public boolean isIsTest() {
		return isTest;
	}

	public void setIsTest(boolean isTest) {
		this.isTest = isTest;
	}
}
