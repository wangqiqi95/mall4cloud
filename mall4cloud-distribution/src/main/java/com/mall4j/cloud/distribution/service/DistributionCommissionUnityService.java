package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.distribution.model.DistributionCommissionUnity;

/**
 * 佣金配置-统一佣金
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
public interface DistributionCommissionUnityService {

	/**
	 * 根据佣金配置-统一佣金id获取佣金配置-统一佣金
	 *
	 * @return 佣金配置-统一佣金
	 */
	DistributionCommissionUnity get();

	/**
	 * 保存佣金配置-统一佣金
	 * @param distributionCommissionUnity 佣金配置-统一佣金
	 */
	void save(DistributionCommissionUnity distributionCommissionUnity);

	/**
	 * 更新佣金配置-统一佣金
	 * @param distributionCommissionUnity 佣金配置-统一佣金
	 */
	void update(DistributionCommissionUnity distributionCommissionUnity);

}
