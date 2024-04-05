package com.mall4j.cloud.user.service;

import com.mall4j.cloud.user.model.UserLevel;

import java.util.List;

/**
 * 等级-权益关联信息
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public interface UserLevelRightsService {

	/**
	 * 保存等级-权益关联信息
	 * @param userLevelId
	 * @param rightIds
	 */
	void save(Long userLevelId, List<Long> rightIds);

	/**
	 * 更新等级-权益关联信息
	 * @param userLevelId 会员等级id
	 * @param rightIds 权益Id列表
	 */
	void update(Long userLevelId, List<Long> rightIds);

	/**
	 * 根据等级id删除等级-权益关联信息
	 * @param userLevelId
	 */
	void deleteByUserLevelId(Long userLevelId);

	/**
	 * 根据权益id查出拥有此权益的会员等级id列表
	 * @param rightId 权益id
	 * @return 会员等级id列表
	 */
	List<UserLevel> listUserLevelIdByRightId(Long rightId);

}
