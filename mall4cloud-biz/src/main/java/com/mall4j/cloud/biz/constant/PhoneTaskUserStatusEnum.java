package com.mall4j.cloud.biz.constant;

/**
 * 0未添加/1添加成功/2添加失败
 */
public enum PhoneTaskUserStatusEnum {

	DEFAULT(0 , "未添加"),
	IN_AUDIT(1 , "添加成功"),
	AUDIT_FAIL(2 , "添加失败");

	private final Integer value;

	private final String desc;

	public Integer value() {
		return value;
	}

	public String desc() {
		return desc;
	}

	PhoneTaskUserStatusEnum(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	
	public static PhoneTaskUserStatusEnum instance(Integer value) {
		PhoneTaskUserStatusEnum[] enums = values();
		for (PhoneTaskUserStatusEnum statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}
	
}
