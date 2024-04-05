package com.mall4j.cloud.group.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.model.QuestionnaireUserAnswerRecordContent;

/**
 * 问卷 会员答题记录内容
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
public interface QuestionnaireUserAnswerRecordContentService {

	/**
	 * 分页获取问卷 会员答题记录内容列表
	 * @param pageDTO 分页参数
	 * @return 问卷 会员答题记录内容列表分页数据
	 */
	PageVO<QuestionnaireUserAnswerRecordContent> page(PageDTO pageDTO);

	/**
	 * 根据问卷 会员答题记录内容id获取问卷 会员答题记录内容
	 *
	 * @param id 问卷 会员答题记录内容id
	 * @return 问卷 会员答题记录内容
	 */
	QuestionnaireUserAnswerRecordContent getById(Long id);

	/**
	 * 保存问卷 会员答题记录内容
	 * @param questionnaireUserAnswerRecordContent 问卷 会员答题记录内容
	 */
	void save(QuestionnaireUserAnswerRecordContent questionnaireUserAnswerRecordContent);

	/**
	 * 更新问卷 会员答题记录内容
	 * @param questionnaireUserAnswerRecordContent 问卷 会员答题记录内容
	 */
	void update(QuestionnaireUserAnswerRecordContent questionnaireUserAnswerRecordContent);

	/**
	 * 根据问卷 会员答题记录内容id删除问卷 会员答题记录内容
	 * @param id 问卷 会员答题记录内容id
	 */
	void deleteById(Long id);
}
