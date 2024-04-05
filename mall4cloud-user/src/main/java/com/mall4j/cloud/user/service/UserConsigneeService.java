package com.mall4j.cloud.user.service;

import com.mall4j.cloud.user.model.UserConsignee;

/**
 * 用户提货人信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-15 17:18:56
 */
public interface UserConsigneeService {

	/**
	 * 根据用户id获取用户提货人信息
	 * @param userId 用户id
	 * @return 用户提货人信息
	 */
	UserConsignee getByUserId(Long userId);

	/**
	 * 保存用户提货人信息
	 * @param userConsignee 用户提货人信息
	 */
	void save(UserConsignee userConsignee);

	/**
	 * 更新用户提货人信息
	 * @param userConsignee 用户提货人信息
	 */
	void update(UserConsignee userConsignee);
}
