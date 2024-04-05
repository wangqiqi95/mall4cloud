package com.mall4j.cloud.common.bean;


import com.mall4j.cloud.common.constant.QiniuZone;

/**
 * 七牛云存储配置信息
 * @author FrozenWatermelon
 */
public class Qiniu {

	private String accessKey;

	private String secretKey;

	private String bucket;

	private QiniuZone zone;

	private Boolean isOpen;

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public QiniuZone getZone() {
		return zone;
	}

	public void setZone(QiniuZone zone) {
		this.zone = zone;
	}

	public Boolean getOpen() {
		return isOpen;
	}

	public void setOpen(Boolean open) {
		isOpen = open;
	}
}
