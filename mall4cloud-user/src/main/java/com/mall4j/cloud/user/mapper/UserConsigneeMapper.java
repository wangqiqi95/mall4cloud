package com.mall4j.cloud.user.mapper;

import com.mall4j.cloud.user.model.UserConsignee;
import org.apache.ibatis.annotations.Param;

/**
 * 用户提货人信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-15 17:18:56
 */
public interface UserConsigneeMapper {

	/**
	 * 根据用户提货人信息id获取用户提货人信息
	 *
	 * @param userId 用户id
	 * @return 用户提货人信息
	 */
	UserConsignee getByUserId(@Param("userId") Long userId);

	/**
	 * 保存用户提货人信息
	 * @param userConsignee 用户提货人信息
	 */
	void save(@Param("userConsignee") UserConsignee userConsignee);

	/**
	 * 更新用户提货人信息
	 * @param userConsignee 用户提货人信息
	 */
	void update(@Param("userConsignee") UserConsignee userConsignee);
}
