package com.mall4j.cloud.biz.constant;

import lombok.Getter;

/**
 * 短链/触点 类型
 * @author Tan
 * @date 2022/11/09
 */
@Getter
public enum SceneType {

	/**
	 * 短链
	 */
	SHORT_LINK(0),

	/**
	 * 触点
	 */
	TENTACLE(1),

	;

	private final Integer value;

	public Integer value() {
		return value;
	}

	SceneType(Integer value) {
		this.value = value;
	}

}
