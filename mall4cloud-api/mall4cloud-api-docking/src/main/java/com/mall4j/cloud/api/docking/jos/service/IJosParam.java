package com.mall4j.cloud.api.docking.jos.service;

import com.mall4j.cloud.api.docking.jos.dto.JosIntefaceContext;

import java.io.Serializable;

/**
 * @description: 标记接口
 * @date 2021/12/23 22:26
 */
public interface IJosParam extends Serializable {

	/**
	 * 当请求参数为
	 * @return
	 */
	default String asJsonPropertiesKey() {
		return "";
	}

	default void setJosContext(JosIntefaceContext context) {

	}
}
