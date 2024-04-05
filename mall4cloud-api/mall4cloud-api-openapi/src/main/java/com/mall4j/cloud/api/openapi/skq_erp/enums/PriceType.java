package com.mall4j.cloud.api.openapi.skq_erp.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 类描述：价格类型
 *
 * @date 2022/1/14 20:38：15
 */
public enum PriceType {

	/**
	 * 吊牌价
	 */
	PRICE(1, "吊牌价"),

	/**
	 * 保护价
	 */
	PROTECTION_PRICE(2, "保护价"),

	/**
	 * 活动价
	 */
	ACTIVITY_PRICE(3, "活动价");

	private int value;

	private String msg;

	PriceType(int value, String msg) {
		this.value = value;
		this.msg = msg;
	}

	public int value() {
		return value;
	}

	public String msg() {
		return msg;
	}


	public static Optional<PriceType> get(int value) {
		return Stream.of(values()).filter(s -> s.value == value).findFirst();
	}
}
