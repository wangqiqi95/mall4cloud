package com.mall4j.cloud.common.constant;

import java.util.Objects;

/**
 * 状态
 * @author yxf
 * @date 2020/11/20
 */
public enum StatusEnum {

	/**
	 * 删除 (逻辑删除)
	 */
	DELETE(-1),

	/**
	 * 禁用/过期/下架
	 */
	DISABLE(0),

	/**
	 * 启用/未过期/上架
	 */
	ENABLE(1),

	/**
	 * 违规下架
	 */
	OFFLINE(2),

	/**
	 * 等待审核
	 */
	WAIT_AUDIT(3),

	/**
	 * 关闭
	 */
	FINISHED(4)
	;

	private final Integer value;

	public Integer value() {
		return value;
	}

	StatusEnum(Integer value) {
		this.value = value;
	}

	public static Boolean offlineStatus (Integer value) {
		StatusEnum[] enums = values();
		for (StatusEnum statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	public static Boolean offlineOrDelete (Integer status) {
		if (Objects.equals(status, OFFLINE.value) || Objects.equals(status, DELETE.value) || Objects.equals(status, DISABLE.value)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

}
