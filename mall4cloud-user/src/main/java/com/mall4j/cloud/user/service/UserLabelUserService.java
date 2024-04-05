package com.mall4j.cloud.user.service;

import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.dto.UserLabelUserDTO;
import com.mall4j.cloud.user.model.UserLabelUser;

import java.util.List;

/**
 * 导购标签用户信息
 *
 * @author ZengFanChang
 * @date 2022-01-08 23:27:34
 */
public interface UserLabelUserService {

	/**
	 * 分页获取导购标签用户信息列表
	 * @param pageDTO 分页参数
	 * @return 导购标签用户信息列表分页数据
	 */
	PageVO<UserApiVO> page(PageDTO pageDTO, UserLabelUserDTO userLabelUserDTO);

	/**
	 * 根据导购标签用户信息id获取导购标签用户信息
	 *
	 * @param id 导购标签用户信息id
	 * @return 导购标签用户信息
	 */
	UserLabelUser getById(Long id);

	/**
	 * 保存导购标签用户信息
	 * @param userLabelUser 导购标签用户信息
	 */
	void save(UserLabelUser userLabelUser);

	/**
	 * 更新导购标签用户信息
	 * @param userLabelUser 导购标签用户信息
	 */
	void update(UserLabelUser userLabelUser);

	/**
	 * 根据导购标签用户信息id删除导购标签用户信息
	 * @param id 导购标签用户信息id
	 */
	void deleteById(Long id);

	void deleteByTag(Long tagId);

	void deleteByTagNotUserIn(Long tagId, List<Long> userIds);

	List<UserLabelUser> listByTag(Long tagId);
}
