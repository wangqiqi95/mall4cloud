package com.mall4j.cloud.common.constant;

import java.util.Objects;

/**
 * 状态
 * @author yxf
 * @date 2020/11/20
 */
public enum DeleteEnum {

	/**
	 * 删除
	 */
	NORMAL(0),

	/**
	 * 正常
	 */
	DELETE(1)
	;

	private final Integer value;

	public Integer value() {
		return value;
	}

	DeleteEnum(Integer value) {
		this.value = value;
	}

	public static Boolean offlineStatus (Integer value) {
		DeleteEnum[] enums = values();
		for (DeleteEnum statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	public static Boolean offlineOrDelete (Integer status) {
		if (Objects.equals(status, DeleteEnum.DELETE) ) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

}
