package com.mall4j.cloud.biz.constant;

/**
 * 类型：
 * 1=部门数据
 * 2=部门成员
 * 3=外部联系人及联系人与部门成员映射关系
 * 4=企微群及群客户信息
 */
public enum CrmPushMsgTypeEnum {

	WX_DEPART("1" , "部门数据"),
	WX_USER("2" , "部门成员"),
	WX_EXTERNAL("3" , "外部联系人及联系人与部门成员映射关系"),
	WX_GROUP("4" , "企微群及群客户信息");

	private final String value;

	private final String desc;

	public String value() {
		return value;
	}

	public String desc() {
		return desc;
	}

	CrmPushMsgTypeEnum(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	
	public static CrmPushMsgTypeEnum instance(Integer value) {
		CrmPushMsgTypeEnum[] enums = values();
		for (CrmPushMsgTypeEnum statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}
	
}
