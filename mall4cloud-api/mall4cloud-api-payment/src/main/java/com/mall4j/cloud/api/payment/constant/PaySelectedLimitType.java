package com.mall4j.cloud.api.payment.constant;

/**
 * 支付配置限制条件选中类型枚举
 */
public enum PaySelectedLimitType {
	
	/**
	 * 未选中/选中
	 */
	UNSELECTED(0,"未选中"),
	SELECTED(1,"选中");
	
	private final Integer value;
	
	private final String desc;
	
	public Integer value() {
		return value;
	}
	
	public String desc() {
		return desc;
	}
	
	PaySelectedLimitType(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	
	public static PaySelectedLimitType instance(Integer value) {
		PaySelectedLimitType[] enums = values();
		for (PaySelectedLimitType statusEnum : enums) {
			if (statusEnum.value.equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}
}
