package com.mall4j.cloud.user.mapper;

import com.mall4j.cloud.user.dto.UserJourneysDTO;
import com.mall4j.cloud.user.model.UserStaffRelationFollowUp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会员跟进记录
 *
 * @author FrozenWatermelon
 * @date 2023-11-13 17:37:14
 */
public interface UserStaffRelationFollowUpMapper {

	/**
	 * 获取会员跟进记录列表
	 * @return 会员跟进记录列表
	 */
	List<UserStaffRelationFollowUp> list();

	/**
	 * 根据会员跟进记录id获取会员跟进记录
	 *
	 * @param id 会员跟进记录id
	 * @return 会员跟进记录
	 */
	UserStaffRelationFollowUp getById(@Param("id") Long id);

	/**
	 * 保存会员跟进记录
	 * @param userStaffRelationFollowUp 会员跟进记录
	 */
	void save(@Param("userStaffRelationFollowUp") UserStaffRelationFollowUp userStaffRelationFollowUp);

	/**
	 * 更新会员跟进记录
	 * @param userStaffRelationFollowUp 会员跟进记录
	 */
	void update(@Param("userStaffRelationFollowUp") UserStaffRelationFollowUp userStaffRelationFollowUp);

	/**
	 * 根据会员跟进记录id删除会员跟进记录
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	List<UserStaffRelationFollowUp> follwUpList(@Param("request")UserJourneysDTO request);

	List<UserStaffRelationFollowUp> updateFollwUpList(@Param("request")UserJourneysDTO request);
}
