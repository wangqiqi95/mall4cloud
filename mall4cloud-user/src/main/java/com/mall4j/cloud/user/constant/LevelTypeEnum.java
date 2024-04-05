package com.mall4j.cloud.user.constant;

/**
 * 等级
 * @author yxf
 * @date 2020/11/20
 */
public enum LevelTypeEnum {

	/**
	 * 普通会员
	 */
	ORDINARY_USER(0, "普通会员"),

	/**
	 * 付费会员
	 */
	PAY_USER(1,"付费会员")
	;

	private final Integer value;

	private final String desc;

	public Integer value() {
		return value;
	}

	public String desc() {
		return desc;
	}

	LevelTypeEnum(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public static LevelTypeEnum instance(Integer value) {
		LevelTypeEnum[] enums = values();
		for (LevelTypeEnum typeEnum : enums) {
			if (typeEnum.value().equals(value)) {
				return typeEnum;
			}
		}
		return null;
	}

}
