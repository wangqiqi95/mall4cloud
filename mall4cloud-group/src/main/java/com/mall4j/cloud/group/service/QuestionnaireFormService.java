package com.mall4j.cloud.group.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.model.QuestionnaireForm;

/**
 * 问卷表单内容
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
public interface QuestionnaireFormService {

	/**
	 * 分页获取问卷表单内容列表
	 * @param pageDTO 分页参数
	 * @return 问卷表单内容列表分页数据
	 */
	PageVO<QuestionnaireForm> page(PageDTO pageDTO);

	/**
	 * 根据问卷表单内容id获取问卷表单内容
	 *
	 * @param id 问卷表单内容id
	 * @return 问卷表单内容
	 */
	QuestionnaireForm getById(Long id);

	/**
	 * 保存问卷表单内容
	 * @param questionnaireForm 问卷表单内容
	 */
	void save(QuestionnaireForm questionnaireForm);

	/**
	 * 更新问卷表单内容
	 * @param questionnaireForm 问卷表单内容
	 */
	void update(QuestionnaireForm questionnaireForm);

	/**
	 * 根据问卷表单内容id删除问卷表单内容
	 * @param id 问卷表单内容id
	 */
	void deleteById(Long id);

	/**
	 * 导出 excel
	 */
	void exportExcel(Long id, Boolean isSubmit);
}
