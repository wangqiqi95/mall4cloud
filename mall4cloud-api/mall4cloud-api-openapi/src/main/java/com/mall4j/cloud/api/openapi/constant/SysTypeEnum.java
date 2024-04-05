package com.mall4j.cloud.api.openapi.constant;

/**
 * 第三方系统类型
 * @author FrozenWatermelon
 * @date 2020/7/2
 */
public enum SysTypeEnum {

	/**
	 * 爱铺货系统
	 */
	IPH(1),

	;

	private final Integer value;

	public Integer value() {
		return value;
	}

	SysTypeEnum(Integer value) {
		this.value = value;
	}
}
