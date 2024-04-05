package com.mall4j.cloud.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mall4j.cloud.group.model.QuestionnaireUserAnswerRecordContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 问卷 会员答题记录内容
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
public interface QuestionnaireUserAnswerRecordContentMapper extends BaseMapper<QuestionnaireUserAnswerRecordContent> {

	/**
	 * 获取问卷 会员答题记录内容列表
	 * @return 问卷 会员答题记录内容列表
	 */
	List<QuestionnaireUserAnswerRecordContent> list();

	/**
	 * 根据问卷 会员答题记录内容id获取问卷 会员答题记录内容
	 *
	 * @param id 问卷 会员答题记录内容id
	 * @return 问卷 会员答题记录内容
	 */
	QuestionnaireUserAnswerRecordContent getById(@Param("id") Long id);

	/**
	 * 保存问卷 会员答题记录内容
	 * @param questionnaireUserAnswerRecordContent 问卷 会员答题记录内容
	 */
	void save(@Param("questionnaireUserAnswerRecordContent") QuestionnaireUserAnswerRecordContent questionnaireUserAnswerRecordContent);

	/**
	 * 更新问卷 会员答题记录内容
	 * @param questionnaireUserAnswerRecordContent 问卷 会员答题记录内容
	 */
	void update(@Param("questionnaireUserAnswerRecordContent") QuestionnaireUserAnswerRecordContent questionnaireUserAnswerRecordContent);

	/**
	 * 根据问卷 会员答题记录内容id删除问卷 会员答题记录内容
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 根据 activityId 查询所有用户提交内容
	 * @param activityId 活动Id
	 * @return list bo
	 */
	default List<QuestionnaireUserAnswerRecordContent> selectAllByActivityId(@Param("activityId") Long activityId){
		return selectList(Wrappers.lambdaQuery(QuestionnaireUserAnswerRecordContent.class)
				.eq(QuestionnaireUserAnswerRecordContent::getActivityId, activityId));
	}

	/**
	 * 统计 user and activityId 所填写的表单内容数量
	 * @param userId 用户ID
	 * @param activityId 活动ID
	 * @return count
	 */
    default Integer selectCountByUserIdAndActivityId(Long userId, Long activityId){
		return selectCount(Wrappers.lambdaQuery(QuestionnaireUserAnswerRecordContent.class)
				.eq(QuestionnaireUserAnswerRecordContent::getUserId, userId)
				.eq(QuestionnaireUserAnswerRecordContent::getActivityId, activityId));
	}
}
