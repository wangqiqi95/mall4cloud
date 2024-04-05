package com.mall4j.cloud.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.user.model.UserLabelUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 导购标签用户信息
 *
 * @author ZengFanChang
 * @date 2022-01-08 23:27:34
 */
public interface UserLabelUserMapper extends BaseMapper<UserLabelUser> {

	/**
	 * 获取导购标签用户信息列表
	 * @return 导购标签用户信息列表
	 */
	List<UserLabelUser> list();

	/**
	 * 根据导购标签用户信息id获取导购标签用户信息
	 *
	 * @param id 导购标签用户信息id
	 * @return 导购标签用户信息
	 */
	UserLabelUser getById(@Param("id") Long id);

	/**
	 * 保存导购标签用户信息
	 * @param userLabelUser 导购标签用户信息
	 */
	void save(@Param("userLabelUser") UserLabelUser userLabelUser);

	/**
	 * 更新导购标签用户信息
	 * @param userLabelUser 导购标签用户信息
	 */
	void update(@Param("userLabelUser") UserLabelUser userLabelUser);

	/**
	 * 根据导购标签用户信息id删除导购标签用户信息
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
