package com.mall4j.cloud.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mall4j.cloud.group.dto.questionnaire.AnswerPageDTO;
import com.mall4j.cloud.group.model.QuestionnaireUserAnswerRecord;
import com.mall4j.cloud.group.vo.questionnaire.QuestionnaireUserAnswerRecordPageVO;
import com.mall4j.cloud.group.vo.questionnaire.QuestionnaireUserUnSubmitVO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 问卷 会员答题记录
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
public interface QuestionnaireUserAnswerRecordMapper extends BaseMapper<QuestionnaireUserAnswerRecord> {

	/**
	 * 获取问卷 会员答题记录列表
	 * @return 问卷 会员答题记录列表
	 */
	List<QuestionnaireUserAnswerRecord> list();

	List<QuestionnaireUserAnswerRecordPageVO> page(AnswerPageDTO param);

	/**
	 * 根据问卷 会员答题记录id获取问卷 会员答题记录
	 *
	 * @param id 问卷 会员答题记录id
	 * @return 问卷 会员答题记录
	 */
	QuestionnaireUserAnswerRecord getById(@Param("id") Long id);

	/**
	 * 保存问卷 会员答题记录
	 * @param questionnaireUserAnswerRecord 问卷 会员答题记录
	 */
	void save(@Param("questionnaireUserAnswerRecord") QuestionnaireUserAnswerRecord questionnaireUserAnswerRecord);

	/**
	 * 更新问卷 会员答题记录
	 * @param questionnaireUserAnswerRecord 问卷 会员答题记录
	 */
	void update(@Param("questionnaireUserAnswerRecord") QuestionnaireUserAnswerRecord questionnaireUserAnswerRecord);

	/**
	 * 根据问卷 会员答题记录id删除问卷 会员答题记录
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 根据 activityId, userIdList 查询所有
	 * @param activityId 活动Id
	 * @param userIdList 用户id集合
	 * @return list bo
	 */
	default List<QuestionnaireUserAnswerRecord> selectAllByActivityIdInAndUserId(Long activityId, Collection<Long> userIdList){
		return selectList(Wrappers.lambdaQuery(QuestionnaireUserAnswerRecord.class)
				.eq(QuestionnaireUserAnswerRecord::getActivityId, activityId)
				.in(QuestionnaireUserAnswerRecord::getUserId, userIdList));
	}

	/**
	 * 用户未提交统计
	 * @param activityId 活动ID
	 * @return list vo
	 */
	List<QuestionnaireUserUnSubmitVO> selectCountUnSubmit(@Param("activityId") Long activityId);

	/**
	 * 增加浏览次数
	 * @param id id
	 */
    void increaseBrowse(@Param("id") Long id);

	/**
	 * 根据 activityId, userIdList 查询一个
	 * @param activityId 活动Id
	 * @param userId 用户id
	 * @return bo
	 */
	default QuestionnaireUserAnswerRecord selectOneByActivityIdInAndUserId(Long activityId, Long userId){
		return selectOne(Wrappers.lambdaQuery(QuestionnaireUserAnswerRecord.class)
				.eq(QuestionnaireUserAnswerRecord::getActivityId, activityId)
				.eq(QuestionnaireUserAnswerRecord::getUserId, userId)
				.last("LIMIT 1"));
	}
}
