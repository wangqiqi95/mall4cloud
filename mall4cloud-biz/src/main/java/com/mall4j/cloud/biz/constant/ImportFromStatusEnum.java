package com.mall4j.cloud.biz.constant;

/**
 * 导入来源：0表格导入/1系统用户
 */
public enum ImportFromStatusEnum {

	DEFAULT(0 , "表格导入"),
	IN_AUDIT(1 , "系统用户");

	private final Integer value;

	private final String desc;

	public Integer value() {
		return value;
	}

	public String desc() {
		return desc;
	}

	ImportFromStatusEnum(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	
	public static ImportFromStatusEnum instance(Integer value) {
		ImportFromStatusEnum[] enums = values();
		for (ImportFromStatusEnum statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}
	
}
