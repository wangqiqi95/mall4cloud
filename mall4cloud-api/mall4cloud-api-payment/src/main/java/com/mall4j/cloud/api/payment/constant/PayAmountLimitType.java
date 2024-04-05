package com.mall4j.cloud.api.payment.constant;

/**
 * 支付配置限制类型
 */
public enum PayAmountLimitType {
	
	/**
	 * 不限/满额
	 */
	NOT_LIMIT(0,"不限"),
	AMOUNT_LIMIT(1,"满额");
	
	private final Integer value;
	
	private final String desc;
	
	public Integer value() {
		return value;
	}
	
	public String desc() {
		return desc;
	}
	
	PayAmountLimitType(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	
	public static PayAmountLimitType instance(Integer value) {
		PayAmountLimitType[] enums = values();
		for (PayAmountLimitType statusEnum : enums) {
			if (statusEnum.value.equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}
}
