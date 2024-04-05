package com.mall4j.cloud.group.constant;

/**
 * 团购活动状态
 * @author yxf
 * @date 2020/11/20
 */
public enum AppGroupActivityStatus {

	/** 未开始 */
	NOT_STARTED(1),

	/** 进行中 */
	UNDER_WAY(2),

	/** 已结束 */
	FINISHED(3)
	;

	private final Integer value;

	public Integer value() {
		return value;
	}

	AppGroupActivityStatus(Integer value) {
		this.value = value;
	}

	public static Boolean offlineStatus (Integer value) {
		AppGroupActivityStatus[] enums = values();
		for (AppGroupActivityStatus statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

}
