package com.mall4j.cloud.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mall4j.cloud.group.model.QuestionnaireUserGiftAddr;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户奖品配送地址
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:11:00
 */
public interface QuestionnaireUserGiftAddrMapper extends BaseMapper<QuestionnaireUserGiftAddr> {

	/**
	 * 获取用户奖品配送地址列表
	 * @return 用户奖品配送地址列表
	 */
	List<QuestionnaireUserGiftAddr> list();

	/**
	 * 根据用户奖品配送地址id获取用户奖品配送地址
	 *
	 * @param id 用户奖品配送地址id
	 * @return 用户奖品配送地址
	 */
	QuestionnaireUserGiftAddr getById(@Param("id") Long id);

	/**
	 * 保存用户奖品配送地址
	 * @param questionnaireUserGiftAddr 用户奖品配送地址
	 */
	void save(@Param("questionnaireUserGiftAddr") QuestionnaireUserGiftAddr questionnaireUserGiftAddr);

	/**
	 * 更新用户奖品配送地址
	 * @param questionnaireUserGiftAddr 用户奖品配送地址
	 */
	void update(@Param("questionnaireUserGiftAddr") QuestionnaireUserGiftAddr questionnaireUserGiftAddr);

	/**
	 * 根据用户奖品配送地址id删除用户奖品配送地址
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 根据 activityId and userId 查询一条信息
	 * @param activityId 活动ID
	 * @param userId 用户ID
	 * @return bo
	 */
    default QuestionnaireUserGiftAddr selectOneByActivityIdAndUserId(Long activityId, Long userId){
		return selectOne(Wrappers.lambdaQuery(QuestionnaireUserGiftAddr.class)
				.eq(QuestionnaireUserGiftAddr::getActivityId, activityId)
				.eq(QuestionnaireUserGiftAddr::getUserId, userId)
				.last("LIMIT 1"));
	}

}
