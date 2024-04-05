package com.mall4j.cloud.common.bean;

/**
 * 支付宝配置
 * @author FrozenWatermelon
 */
public class Alipay {

	private String appId;

	/**
	 * 应用公钥证书
	 */
	private String appCertPath;

	/**
	 * 支付宝公钥证书
	 */
	private String alipayCertPath;

	/**
	 * 支付宝根证书
	 */
	private String alipayRootCertPath;

	/**
	 * 应用私钥
	 */
	private String appPrivateKey;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppCertPath() {
		return appCertPath;
	}

	public void setAppCertPath(String appCertPath) {
		this.appCertPath = appCertPath;
	}

	public String getAlipayCertPath() {
		return alipayCertPath;
	}

	public void setAlipayCertPath(String alipayCertPath) {
		this.alipayCertPath = alipayCertPath;
	}

	public String getAlipayRootCertPath() {
		return alipayRootCertPath;
	}

	public void setAlipayRootCertPath(String alipayRootCertPath) {
		this.alipayRootCertPath = alipayRootCertPath;
	}

	public String getAppPrivateKey() {
		return appPrivateKey;
	}

	public void setAppPrivateKey(String appPrivateKey) {
		this.appPrivateKey = appPrivateKey;
	}
}
