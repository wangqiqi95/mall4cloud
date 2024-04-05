package com.mall4j.cloud.api.openapi.skq_erp.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 类描述：库存类型
 *
 * @date 2022/1/13 8:24：48
 */
public enum StockType {
	/**
	 * 共享库存
	 */
	SHARE_STOCK(1, "共享库存"),

	/**
	 * 门店库存
	 */
	SHOP_STOCK(2, "门店库存");

	private int value;

	private String msg;

	StockType(int value, String msg) {
		this.value = value;
		this.msg = msg;
	}

	public int value() {
		return value;
	}

	public String msg() {
		return msg;
	}


	public static Optional<StockType> get(int value) {
		return Stream.of(values()).filter(s -> s.value == value).findFirst();
	}
}
