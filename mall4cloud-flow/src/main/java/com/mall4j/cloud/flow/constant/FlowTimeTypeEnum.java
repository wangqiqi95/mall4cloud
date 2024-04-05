package com.mall4j.cloud.flow.constant;


/**
 * 流量筛选的时间类型
 * @author YXF
 * @date 2021-05-21
 */
public enum FlowTimeTypeEnum {
	/** 日*/
	DAY(1, 1),
	/** 周 */
	WEEK(2, 1),
	/** 月 */
	MONTH(3, 1),
	/** 今日实时 */
	REAL_TIME(4, 1),
	/** 近七天 */
	NEARLY_WEEK(5, 7),
	/** 近30天 */
	NEARLY_MONTH(6, 30),
	/** 自定义 */
	CUSTOM(7, 0);

	private Integer id;

	private Integer num;

	public Integer value() {
		return id;
	}
	public Integer getNum() {
		return num;
	}

	FlowTimeTypeEnum(Integer id, Integer num){
		this.id = id;
		this.num = num;
	}

	public static FlowTimeTypeEnum instance(Integer value) {
		FlowTimeTypeEnum[] enums = values();
		for (FlowTimeTypeEnum statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return statusEnum;
			}
		}
		return DAY;
	}

	public static FlowTimeTypeEnum[] allEnum() {
		FlowTimeTypeEnum[] enums = values();
		return enums;
	}
}
