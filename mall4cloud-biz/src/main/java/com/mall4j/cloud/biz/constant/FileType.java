package com.mall4j.cloud.biz.constant;

/**
 * 文件类型
 * @author YXF
 * @date 2021/03/30
 */
public enum FileType {

	/**
	 * 图片
	 */
	IMAGE(1),

	/**
	 * 视频
	 */
	VIDEO(2),

	/**
	 * 文件
	 */
	FILE(3),
;

	private final Integer value;

	public Integer value() {
		return value;
	}

	FileType(Integer value) {
		this.value = value;
	}

}
