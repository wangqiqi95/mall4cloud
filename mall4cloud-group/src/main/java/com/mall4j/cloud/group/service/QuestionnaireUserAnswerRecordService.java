package com.mall4j.cloud.group.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.model.QuestionnaireUserAnswerRecord;

/**
 * 问卷 会员答题记录
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
public interface QuestionnaireUserAnswerRecordService extends IService<QuestionnaireUserAnswerRecord> {

	/**
	 * 分页获取问卷 会员答题记录列表
	 * @param pageDTO 分页参数
	 * @return 问卷 会员答题记录列表分页数据
	 */
	PageVO<QuestionnaireUserAnswerRecord> page(PageDTO pageDTO);

	/**
	 * 根据问卷 会员答题记录id获取问卷 会员答题记录
	 *
	 * @param id 问卷 会员答题记录id
	 * @return 问卷 会员答题记录
	 */
	QuestionnaireUserAnswerRecord getById(Long id);


	/**
	 * 更新问卷 会员答题记录
	 * @param questionnaireUserAnswerRecord 问卷 会员答题记录
	 */
	void update(QuestionnaireUserAnswerRecord questionnaireUserAnswerRecord);

	/**
	 * 根据问卷 会员答题记录id删除问卷 会员答题记录
	 * @param id 问卷 会员答题记录id
	 */
	void deleteById(Long id);

	QuestionnaireUserAnswerRecord getOrSave(Long activityId, Long userId, Long storeId, Boolean isIncreaseBrowse, Boolean isSendSubscribe);

	/**
	 * 埋点，用于增加问卷浏览次数
	 * @param userId 用户ID
	 * @param activityId 活动ID
	 */
	void pointBrowseCount(Long userId, Long activityId);
}
