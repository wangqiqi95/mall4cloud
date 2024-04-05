package com.mall4j.cloud.api.openapi.skq_erp.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @description 物流公司编码类型
 * @date 2021/12/28 17:53：09
 */
public enum DeliverType {
	/**
	 * 阿里
	 */
	ALI(1, "阿里"),

	/**
	 * 快递鸟
	 */
	KD_NIAO(2, "快递鸟"),

	/**
	 * 快递100
	 */
	KD_100(3, "快递100"),

	/**
	 * 未知
	 */
	UNKNOWN(4, "未知");


	int code;

	String desc;

	DeliverType(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int code() {
		return code;
	}

	public String desc() {
		return desc;
	}

	public static Optional<DeliverType> getByCode(int code) {
		return Stream.of(values()).filter(c -> c.code == code).findFirst();
	}
}
