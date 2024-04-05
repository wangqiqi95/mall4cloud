package com.mall4j.cloud.user.constant;

/**
 * 会员等级更新状态
 * @author yxf
 * @date 2020/11/20
 */
public enum UpdateStatusEnum {

	/**
	 * 未更新
	 */
	WAIT_UPDATE(0),

	/**
	 * 已更新
	 */
	UPDATE(1)
	;

	private final Integer value;

	public Integer value() {
		return value;
	}

	UpdateStatusEnum(Integer value) {
		this.value = value;
	}

}
