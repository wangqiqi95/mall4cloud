package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionCommissionActivityDTO;
import com.mall4j.cloud.distribution.dto.DistributionCommissionActivitySearchDTO;
import com.mall4j.cloud.distribution.dto.DistributionCommissionActivityUpdateDTO;
import com.mall4j.cloud.distribution.model.DistributionCommissionActivity;

/**
 * 佣金配置-活动佣金
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
public interface DistributionCommissionActivityService {

	/**
	 * 分页获取佣金配置-活动佣金列表
	 * @param pageDTO 分页参数
	 * @param commissionActivitySearchDTO 查询参数
	 * @return 佣金配置-活动佣金列表分页数据
	 */
	PageVO<DistributionCommissionActivity> page(PageDTO pageDTO, DistributionCommissionActivitySearchDTO commissionActivitySearchDTO);

	/**
	 * 根据佣金配置-活动佣金id获取佣金配置-活动佣金
	 *
	 * @param id 佣金配置-活动佣金id
	 * @return 佣金配置-活动佣金
	 */
	DistributionCommissionActivity getById(Long id);

	/**
	 * 保存佣金配置-活动佣金
	 * @param commissionActivityDTO 佣金配置-活动佣金
	 */
	void save(DistributionCommissionActivityDTO commissionActivityDTO);

	/**
	 * 更新佣金配置-活动佣金
	 * @param commissionActivityDTO 佣金配置-活动佣金
	 */
	void update(DistributionCommissionActivityDTO commissionActivityDTO);

	/**
	 * 更新佣金配置-活动佣金状态(支持更新活动状态、活动比例等状态)
	 * @param distributionCommissionActivityUpdateDTO
	 */
	void updateStatus(DistributionCommissionActivityUpdateDTO distributionCommissionActivityUpdateDTO);

	/**
	 * 更新佣金配置-活动佣金比例(支持更新导购、微客、发展佣金)
	 * @param distributionCommissionActivityUpdateDTO
	 */
	void updateRate(DistributionCommissionActivityUpdateDTO distributionCommissionActivityUpdateDTO);

	/**
	 * 更新佣金配置-活动佣金有效期
	 * @param distributionCommissionActivityUpdateDTO
	 */
	void updateActivityTime(DistributionCommissionActivityUpdateDTO distributionCommissionActivityUpdateDTO);

	/**
	 * 根据佣金配置-活动佣金id删除佣金配置-活动佣金
	 * @param id 佣金配置-活动佣金id
	 */
	void deleteById(Long id);

	/**
	 * 更新佣金配置-活动佣金优先级
	 * @param sourceId 原活动ID
	 * @param targetId 目标活动ID
	 */
	void updatePriority(Long sourceId, Long targetId);

}
