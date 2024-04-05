package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.UserExtension;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户扩展信息
 *
 * @author FrozenWatermelon
 * @date 2022-04-06 22:30:38
 */
public interface UserExtensionMapper {

	/**
	 * 获取用户扩展信息列表
	 * @return 用户扩展信息列表
	 */
	List<UserExtension> list();

	/**
	 * 根据用户扩展信息id获取用户扩展信息
	 *
	 * @param userExtensionId 用户扩展信息id
	 * @return 用户扩展信息
	 */
	UserExtension getByUserExtensionId(@Param("userExtensionId") Long userExtensionId);

	/**
	 * 保存用户扩展信息
	 * @param userExtension 用户扩展信息
	 */
	void save(@Param("userExtension") UserExtension userExtension);

	void batchSave(@Param("userExtensions") List<UserExtension> userExtension);

    void batchSave2(@Param("userExtensions") List<UserExtension> userExtension);

    void batchSave3(@Param("userExtensions") List<UserExtension> userExtension);

	/**
	 * 更新用户扩展信息
	 * @param userExtension 用户扩展信息
	 */
	void update(@Param("userExtension") UserExtension userExtension);

	/**
	 * 根据用户扩展信息id删除用户扩展信息
	 * @param userExtensionId
	 */
	void deleteById(@Param("userExtensionId") Long userExtensionId);
}
