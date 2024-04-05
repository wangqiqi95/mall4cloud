package com.mall4j.cloud.api.discount.constant;

/**
 * 满减规则
 * @author FrozenWatermelon
 */
public enum DiscountRule {

	/**
	 * 满钱减钱
	 * M money
	 */
	M2M(0),
//	/**
//	 * 满件减钱
//	 * P piece
//	 */
//	P2M(1),
//	/**
//	 * 满钱打折
//	 * Discount
//	 */
//	M2D(2),
	/**
	 * 满件打折
	 * Discount
	 */
	P2D(3)
	;

	private final Integer num;

	public Integer value() {
		return num;
	}

	DiscountRule(Integer num){
		this.num = num;
	}

	public static DiscountRule instance(Integer value) {
		DiscountRule[] enums = values();
		for (DiscountRule statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}
}
