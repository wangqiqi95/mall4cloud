package com.mall4j.cloud.common.product.constant;

/**
 * 商品类型
 * @author yxf
 * @date 2020/11/20
 */
public enum SpuType {

	/**
	 * 普通商品类型
	 */
	NORMAL(0),

	/**
	 * 团购商品类型
	 */
	GROUP(1),
	/**
	 * 秒杀商品类型
	 */
	SECKILL(2),
	/**
	 * 积分商品类型
	 */
	SCORE(3)
	;

	private final Integer value;

	public Integer value() {
		return value;
	}

	SpuType(Integer value) {
		this.value = value;
	}

}
