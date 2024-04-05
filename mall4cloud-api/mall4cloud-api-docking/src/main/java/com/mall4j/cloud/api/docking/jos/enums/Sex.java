package com.mall4j.cloud.api.docking.jos.enums;

/**
 * @description: 性别枚举类
 * @date 2021/12/23 23:06
 */
public enum Sex {
	MALE(0, "男"),
	FEMALE(1, "女"),
	;

	private int code;

	private String desc;

	Sex(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int code() {
		return code;
	}

	public String desc() {
		return desc;
	}
}
