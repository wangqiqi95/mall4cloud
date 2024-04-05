package com.mall4j.cloud.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.group.dto.questionnaire.QuestionnaireShopCountDTO;
import com.mall4j.cloud.group.model.QuestionnaireShop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 问卷适用门店
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
public interface QuestionnaireShopMapper extends BaseMapper<QuestionnaireShop> {

	/**
	 * 获取问卷适用门店列表
	 * @return 问卷适用门店列表
	 */
	List<QuestionnaireShop> list();

	/**
	 * 根据问卷适用门店id获取问卷适用门店
	 *
	 * @param id 问卷适用门店id
	 * @return 问卷适用门店
	 */
	QuestionnaireShop getById(@Param("id") Long id);

	/**
	 * 保存问卷适用门店
	 * @param questionnaireShop 问卷适用门店
	 */
	void save(@Param("questionnaireShop") QuestionnaireShop questionnaireShop);

	void saveBatch(@Param("questionnaireShops") List<QuestionnaireShop> questionnaireShops);

	/**
	 * 更新问卷适用门店
	 * @param questionnaireShop 问卷适用门店
	 */
	void update(@Param("questionnaireShop") QuestionnaireShop questionnaireShop);

	/**
	 * 根据问卷适用门店id删除问卷适用门店
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	void deleteByActivityId(@Param("activityId") Long activityId);

	/**
	 * 统计活动涉及的门店数量
	 * @param activityIdList 活动Id
	 * @return list count DTO
	 */
    List<QuestionnaireShopCountDTO> selectAllCountByActivityIds(@Param("activityIdList") List<Long> activityIdList);
}
