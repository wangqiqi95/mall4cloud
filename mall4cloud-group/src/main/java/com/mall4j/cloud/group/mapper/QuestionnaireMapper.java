package com.mall4j.cloud.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.group.dto.questionnaire.QuestionnairePageDTO;
import com.mall4j.cloud.group.model.Questionnaire;
import com.mall4j.cloud.group.model.QuestionnaireShop;
import com.mall4j.cloud.group.vo.questionnaire.QuestionnaireDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 问卷信息表
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
public interface QuestionnaireMapper extends BaseMapper<Questionnaire> {

	/**
	 * 获取问卷信息表列表
	 * @return 问卷信息表列表
	 */
	List<QuestionnaireDetailVO> list(QuestionnairePageDTO param);

	/**
	 * 根据问卷信息表id获取问卷信息表
	 *
	 * @param id 问卷信息表id
	 * @return 问卷信息表
	 */
	Questionnaire getById(@Param("id") Long id);
	/**
	 * 根据问卷信息表id获取问卷信息表
	 *
	 * @param id 问卷信息表id
	 * @return 问卷信息表
	 */
	Questionnaire getDetailById(@Param("id") Long id);

	/**
	 * 保存问卷信息表
	 * @param questionnaire 问卷信息表
	 */
	void save(@Param("questionnaire") Questionnaire questionnaire);

	/**
	 * 更新问卷信息表
	 * @param questionnaire 问卷信息表
	 */
	void update(@Param("questionnaire") Questionnaire questionnaire);

	/**
	 * 根据问卷信息表id删除问卷信息表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
