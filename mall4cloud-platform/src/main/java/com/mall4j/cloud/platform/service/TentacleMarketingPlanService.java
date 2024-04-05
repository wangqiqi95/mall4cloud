package com.mall4j.cloud.platform.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.platform.model.TentacleMarketingPlan;

/**
 * 触点作业批次
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
public interface TentacleMarketingPlanService {

	/**
	 * 分页获取触点作业批次列表
	 * @param pageDTO 分页参数
	 * @return 触点作业批次列表分页数据
	 */
	PageVO<TentacleMarketingPlan> page(PageDTO pageDTO);

	/**
	 * 根据触点作业批次id获取触点作业批次
	 *
	 * @param id 触点作业批次id
	 * @return 触点作业批次
	 */
	TentacleMarketingPlan getById(Long id);

	/**
	 * 保存触点作业批次
	 * @param tentacleMarketingPlan 触点作业批次
	 */
	void save(TentacleMarketingPlan tentacleMarketingPlan);

	/**
	 * 更新触点作业批次
	 * @param tentacleMarketingPlan 触点作业批次
	 */
	void update(TentacleMarketingPlan tentacleMarketingPlan);

	/**
	 * 根据触点作业批次id删除触点作业批次
	 * @param id 触点作业批次id
	 */
	void deleteById(Long id);
}
