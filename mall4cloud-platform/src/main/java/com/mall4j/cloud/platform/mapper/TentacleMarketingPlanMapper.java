package com.mall4j.cloud.platform.mapper;

import com.mall4j.cloud.platform.model.TentacleMarketingPlan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 触点作业批次
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
public interface TentacleMarketingPlanMapper {

	/**
	 * 获取触点作业批次列表
	 * @return 触点作业批次列表
	 */
	List<TentacleMarketingPlan> list();

	/**
	 * 根据触点作业批次id获取触点作业批次
	 *
	 * @param id 触点作业批次id
	 * @return 触点作业批次
	 */
	TentacleMarketingPlan getById(@Param("id") Long id);

	/**
	 * 保存触点作业批次
	 * @param tentacleMarketingPlan 触点作业批次
	 */
	void save(@Param("tentacleMarketingPlan") TentacleMarketingPlan tentacleMarketingPlan);

	/**
	 * 更新触点作业批次
	 * @param tentacleMarketingPlan 触点作业批次
	 */
	void update(@Param("tentacleMarketingPlan") TentacleMarketingPlan tentacleMarketingPlan);

	/**
	 * 根据触点作业批次id删除触点作业批次
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
