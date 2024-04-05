package com.mall4j.cloud.user.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.model.UserStaffRelationFollowUp;

/**
 * 会员跟进记录
 *
 * @author FrozenWatermelon
 * @date 2023-11-13 17:37:14
 */
public interface UserStaffRelationFollowUpService {

	/**
	 * 分页获取会员跟进记录列表
	 * @param pageDTO 分页参数
	 * @return 会员跟进记录列表分页数据
	 */
	PageVO<UserStaffRelationFollowUp> page(PageDTO pageDTO);

	/**
	 * 根据会员跟进记录id获取会员跟进记录
	 *
	 * @param id 会员跟进记录id
	 * @return 会员跟进记录
	 */
	UserStaffRelationFollowUp getById(Long id);

	/**
	 * 保存会员跟进记录
	 * @param userStaffRelationFollowUp 会员跟进记录
	 */
	void save(UserStaffRelationFollowUp userStaffRelationFollowUp);

	/**
	 * 更新会员跟进记录
	 * @param userStaffRelationFollowUp 会员跟进记录
	 */
	void update(UserStaffRelationFollowUp userStaffRelationFollowUp);

	/**
	 * 根据会员跟进记录id删除会员跟进记录
	 * @param id 会员跟进记录id
	 */
	void deleteById(Long id);
}
