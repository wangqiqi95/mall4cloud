package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.DistributionCommissionActivitySearchDTO;
import com.mall4j.cloud.distribution.model.DistributionCommissionActivity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 佣金配置-活动佣金
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
public interface DistributionCommissionActivityMapper {

	/**
	 * 获取佣金配置-活动佣金列表
	 *
	 * @param commissionActivitySearchDTO 查询参数
	 * @return 佣金配置-活动佣金列表
	 */
	List<DistributionCommissionActivity> list(@Param("commissionActivitySearchDTO") DistributionCommissionActivitySearchDTO commissionActivitySearchDTO);

	/**
	 * 根据佣金配置-活动佣金id获取佣金配置-活动佣金
	 *
	 * @param id 佣金配置-活动佣金id
	 * @return 佣金配置-活动佣金
	 */
	DistributionCommissionActivity getById(@Param("id") Long id);

	/**
	 * 保存佣金配置-活动佣金
	 * @param distributionCommissionActivity 佣金配置-活动佣金
	 */
	void save(@Param("distributionCommissionActivity") DistributionCommissionActivity distributionCommissionActivity);

	/**
	 * 更新佣金配置-活动佣金
	 * @param distributionCommissionActivity 佣金配置-活动佣金
	 */
	void update(@Param("distributionCommissionActivity") DistributionCommissionActivity distributionCommissionActivity);

	/**
	 * 根据佣金配置-活动佣金id删除佣金配置-活动佣金
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 获取最大的优先级值
	 * @return
	 */
	Integer getMaxPriority();


	/**
	 * 更新活动状态为失效
	 */
	void updateActivityStatusByTime();


}
