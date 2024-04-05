package com.mall4j.cloud.user.constant;

/**
 * 积分类型
 * @author lhd
 */
public enum ScoreIoTypeEnum {

	/**
	 * 0.支出
	 */
	EXPENDITURE(0),
	/**
	 * 1. 收入
	 */
	INCOME(1),
	;


	private final Integer num;

	public Integer value() {
		return num;
	}

	ScoreIoTypeEnum(Integer num){
		this.num = num;
	}

	public static ScoreIoTypeEnum instance(Integer value) {
		ScoreIoTypeEnum[] enums = values();
		for (ScoreIoTypeEnum statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}
}
