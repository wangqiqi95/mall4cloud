package com.mall4j.cloud.biz.constant;

/**
 * 文件上传存储类型
 * @author FrozenWatermelon
 * @date 2021/01/20
 */
public enum OssType {

	/**
	 * 阿里云oss
	 */
	ALI(0),

	/**
	 * minio
	 */
	MINIO(1),

	/**
	 * 腾讯云COS
	 */
	Q_CLOUD(2),

	/**
	 * 亚马逊s3
	 */
	AMAZON_S3(3),
;

	private final Integer value;

	public Integer value() {
		return value;
	}

	OssType(Integer value) {
		this.value = value;
	}

}
