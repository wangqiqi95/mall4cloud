package com.mall4j.cloud.group.constant;

/**
 * 团购活动状态
 * @author yxf
 * @date 2020/11/20
 */
public enum GroupActivityStatusEnum {

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
	 * 失效
	 */
	EXPIRED(4),
	;

	private final Integer value;

	public Integer value() {
		return value;
	}

	GroupActivityStatusEnum(Integer value) {
		this.value = value;
	}

	public static Boolean offlineStatus (Integer value) {
		GroupActivityStatusEnum[] enums = values();
		for (GroupActivityStatusEnum statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

}
