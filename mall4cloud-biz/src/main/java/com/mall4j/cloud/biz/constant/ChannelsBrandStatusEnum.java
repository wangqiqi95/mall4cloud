package com.mall4j.cloud.biz.constant;

/**
 * 视频号4.0品牌状态枚举
 */
public enum ChannelsBrandStatusEnum {
	
	DEFAULT(0 , "默认"),
	IN_AUDIT(1 , "审核中"),
	AUDIT_FAIL(2 , "审核失败"),
	AUDIT_SUCCESS(3 , "审核成功"),
	REVOCATION(4 , "已撤回"),
	IN_EXPIRED(5 , "即将过期"),
	EXPIRED(6 , "已过期");
	
	private final Integer value;
	
	private final String desc;
	
	public Integer value() {
		return value;
	}
	
	public String desc() {
		return desc;
	}
	
	ChannelsBrandStatusEnum(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	
	public static ChannelsBrandStatusEnum instance(Integer value) {
		ChannelsBrandStatusEnum[] enums = values();
		for (ChannelsBrandStatusEnum statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}
	
}
