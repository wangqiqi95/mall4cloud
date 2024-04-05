package com.mall4j.cloud.api.user.constant;

import lombok.Getter;

/**
 * 触达类型
 * @author Tan
 * @date 2023/3/8
 */
@Getter
public enum FinalSendTypeEnum {

	/**
	 * 0:非最终发送
	 */
	NO(0),

	/**
	 * 1:最终发送（完成）
	 */
	YES(1),

	;

	private final Integer value;

	public Integer value() {
		return value;
	}

	FinalSendTypeEnum(Integer value) {
		this.value = value;
	}

}
