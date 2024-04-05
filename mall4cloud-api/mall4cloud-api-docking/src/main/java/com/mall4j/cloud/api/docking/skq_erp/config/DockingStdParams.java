package com.mall4j.cloud.api.docking.skq_erp.config;

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
public class DockingStdParams {

	@Value("${std.userName:}")
	private String userName;

	@Value("${std.userKey:}")
	private String userKey;

	@Value("${std.secretkey:}")
	private String secretkey;

	/**
	 * 中台接口域名/ip:port
	 */
	@Value("${std.url:}")
	private String url;

	/**
	 * token的超时时间,单位：秒
	 */
	@Value("${std.tokenExpire:60}")
	private long tokenExpire;

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getTokenExpire() {
		return tokenExpire;
	}

	public void setTokenExpire(long tokenExpire) {
		this.tokenExpire = tokenExpire;
	}

	public String getSecretkey() {
		return secretkey;
	}

	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}
}
