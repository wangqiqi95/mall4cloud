package com.mall4j.cloud.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.group.model.QuestionnaireForm;
import com.mall4j.cloud.group.model.QuestionnaireGift;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 问卷表单内容
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
public interface QuestionnaireFormMapper extends BaseMapper<QuestionnaireGift> {

	/**
	 * 获取问卷表单内容列表
	 * @return 问卷表单内容列表
	 */
	List<QuestionnaireForm> list();

	/**
	 * 根据问卷表单内容id获取问卷表单内容
	 *
	 * @param id 问卷表单内容id
	 * @return 问卷表单内容
	 */
	QuestionnaireForm getById(@Param("id") Long id);

	/**
	 * 保存问卷表单内容
	 * @param questionnaireForm 问卷表单内容
	 */
	void save(@Param("questionnaireForm") QuestionnaireForm questionnaireForm);

	/**
	 * 更新问卷表单内容
	 * @param questionnaireForm 问卷表单内容
	 */
	void update(@Param("questionnaireForm") QuestionnaireForm questionnaireForm);

	/**
	 * 根据问卷表单内容id删除问卷表单内容
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
