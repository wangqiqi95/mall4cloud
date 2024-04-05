package com.mall4j.cloud.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mall4j.cloud.group.model.QuestionnaireUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 问卷会员名单
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
public interface QuestionnaireUserMapper extends BaseMapper<QuestionnaireUser> {

	/**
	 * 获取问卷会员名单列表
	 * @return 问卷会员名单列表
	 */
	List<QuestionnaireUser> list();

	/**
	 * 根据问卷会员名单id获取问卷会员名单
	 *
	 * @param id 问卷会员名单id
	 * @return 问卷会员名单
	 */
	QuestionnaireUser getById(@Param("id") Long id);

	/**
	 * 保存问卷会员名单
	 * @param questionnaireUser 问卷会员名单
	 */
	void save(@Param("questionnaireUser") QuestionnaireUser questionnaireUser);

	void saveBatch(@Param("questionnaireUsers") List<QuestionnaireUser> questionnaireUsers);

	/**
	 * 更新问卷会员名单
	 * @param questionnaireUser 问卷会员名单
	 */
	void update(@Param("questionnaireUser") QuestionnaireUser questionnaireUser);

	/**
	 * 根据问卷会员名单id删除问卷会员名单
	 * @param id
	 */
	void deleteById(@Param("id") Long id);


	void deleteByActivityId(@Param("activityId") Long activityId);

	/**
	 * 根据 activityId 查询绑定名单
	 * @param activityId 活动Id
	 * @return list bo
	 */
    default List<QuestionnaireUser> selectListByActivityId(Long activityId){
		return selectList(Wrappers.lambdaQuery(QuestionnaireUser.class).eq(QuestionnaireUser::getActivityId, activityId));
	}

	/**
	 * 统计活动涉及用户数
	 * @param activityId 活动ID
	 * @return count
	 */
    default Integer selectCountByActivity(Long activityId){
		return selectCount(Wrappers.lambdaQuery(QuestionnaireUser.class).eq(QuestionnaireUser::getActivityId, activityId));
	}

	/**
	 * 根据 activityId and removeUserIds 删除活动用户名单
	 * @param activityId 活动ID
	 * @param removeUserIds 需要删除的用户ID集合
	 */
    default void deleteByActivityIdAndUserId(Long activityId, List<Long> removeUserIds){
		delete(Wrappers.lambdaQuery(QuestionnaireUser.class)
				.eq(QuestionnaireUser::getActivityId, activityId)
				.in(QuestionnaireUser::getUserId, removeUserIds));
	}

	/**
	 * 根据 activityId and userId 查询名单
	 * @param activityId 活动ID
	 * @param userIdList 用户ID集合
	 * @return list
	 */
	default List<QuestionnaireUser> selectListByActivityIdAndUserIds(Long activityId, List<Long> userIdList){
		return selectList(Wrappers.lambdaQuery(QuestionnaireUser.class)
				.eq(QuestionnaireUser::getActivityId, activityId)
				.in(QuestionnaireUser::getUserId, userIdList));
	}
}
