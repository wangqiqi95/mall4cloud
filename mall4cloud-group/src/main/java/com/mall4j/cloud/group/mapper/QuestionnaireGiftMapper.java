package com.mall4j.cloud.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mall4j.cloud.group.model.Questionnaire;
import com.mall4j.cloud.group.model.QuestionnaireGift;
import com.mall4j.cloud.group.model.QuestionnaireUserGiftAddr;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 问卷奖品清单 实物奖品维护
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
public interface QuestionnaireGiftMapper extends BaseMapper<QuestionnaireGift> {

	/**
	 * 获取问卷奖品清单 实物奖品维护列表
	 * @return 问卷奖品清单 实物奖品维护列表
	 */
	List<QuestionnaireGift> list();

	/**
	 * 根据问卷奖品清单 实物奖品维护id获取问卷奖品清单 实物奖品维护
	 *
	 * @param id 问卷奖品清单 实物奖品维护id
	 * @return 问卷奖品清单 实物奖品维护
	 */
	QuestionnaireGift getById(@Param("id") Long id);

	/**
	 * 保存问卷奖品清单 实物奖品维护
	 * @param questionnaireGift 问卷奖品清单 实物奖品维护
	 */
	void save(@Param("questionnaireGift") QuestionnaireGift questionnaireGift);

	/**
	 * 更新问卷奖品清单 实物奖品维护
	 * @param questionnaireGift 问卷奖品清单 实物奖品维护
	 */
	void update(@Param("questionnaireGift") QuestionnaireGift questionnaireGift);

	/**
	 * 根据问卷奖品清单 实物奖品维护id删除问卷奖品清单 实物奖品维护
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

    /**
     * 根据 activityId查询单个
     * @param activityId 活动Id
     * @return bo
     */
	default QuestionnaireGift selectByActivityId(Long activityId){
		return selectOne(Wrappers.lambdaQuery(QuestionnaireGift.class)
				.eq(QuestionnaireGift::getActivityId, activityId)
				.last("LIMIT 1"));
	};

}
