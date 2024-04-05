package com.mall4j.cloud.api.payment.constant;

/**
 * 支付配置会员类型限制枚举
 */
public enum PayMemberLimitType {
	
	/**
	 * 不限会员/指定会员
	 */
	NOT_LIMIT(0,"不限会员"),
	LIMIT(1,"指定会员");
	
	private final Integer value;
	
	private final String desc;
	
	public Integer value() {
		return value;
	}
	
	public String desc() {
		return desc;
	}
	
	PayMemberLimitType(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	
	public static PayMemberLimitType instance(Integer value) {
		PayMemberLimitType[] enums = values();
		for (PayMemberLimitType statusEnum : enums) {
			if (statusEnum.value.equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}
}
