package com.mall4j.cloud.user.constant;

/**
 * 会员等级折扣商品类型
 * @author yxf
 * @date 2020/11/20
 */
public enum DiscountTypeEnum {

	/**
	 * 全部商品
	 */
	ALL_SPU(0),

	/**
	 * 分类下的商品
	 */
	CATEGORY_SPU(1)
	;

	private final Integer value;

	public Integer value() {
		return value;
	}

	DiscountTypeEnum(Integer value) {
		this.value = value;
	}

}
