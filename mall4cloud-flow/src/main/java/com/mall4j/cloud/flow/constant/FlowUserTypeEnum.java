package com.mall4j.cloud.flow.constant;


/**
 * 页面操作
 * @author
 */
public enum FlowUserTypeEnum {
	/** 新用户 */
	NEW_USER(0),
	/** 旧用户*/
	OLDER_USER(1),
	/** 未登陆用户 */
	NOT_LOGIN(2);

	private Integer id;

	public Integer value() {
		return id;
	}

	FlowUserTypeEnum(Integer id){
		this.id = id;
	}

	public static FlowUserTypeEnum instance(String value) {
		FlowUserTypeEnum[] enums = values();
		for (FlowUserTypeEnum statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}

	public static FlowUserTypeEnum[] allEnum() {
		FlowUserTypeEnum[] enums = values();
		return enums;
	}
}
