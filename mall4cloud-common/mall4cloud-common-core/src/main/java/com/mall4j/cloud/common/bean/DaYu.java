package com.mall4j.cloud.common.bean;

/**
 * 阿里大鱼配置信息
 * @author FrozenWatermelon
 */
public class DaYu {

	private String accessKeyId;

	private String accessKeySecret;

	private String signName;

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}
}
