package com.mall4j.cloud.user.constant;

import lombok.Getter;

/**
 * 群发类型
 * @author Tan
 * @date 2023/3/8
 */
@Getter
public enum GroupPushTaskSendModelEnum {

	/**
	 * 1:1v1
	 */
	TO_ONCE_PUSH(1, "点对点发送"),

	/**
	 * 2:批量群发
	 */
	TO_GROUP_PUSH(2, "群发"),

	;

	private final Integer value;
	private final String sendModel;

	public Integer value() {
		return value;
	}


	GroupPushTaskSendModelEnum(Integer value, String sendModel) {
		this.value = value;
		this.sendModel = sendModel;
	}

}
