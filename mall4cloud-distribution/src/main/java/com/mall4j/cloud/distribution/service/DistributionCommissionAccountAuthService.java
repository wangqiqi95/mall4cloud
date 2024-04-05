package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.distribution.dto.DistributionCommissionAccountAuthDTO;
import com.mall4j.cloud.distribution.model.DistributionCommissionAccountAuth;

/**
 * 佣金管理-佣金账户-认证
 *
 * @author gww
 * @date 2022-01-31 12:15:41
 */
public interface DistributionCommissionAccountAuthService {


	/**
	 * 通过身份类型和用户id查询佣金账户认证信息
	 *
	 * @param identityType 身份类型 1-导购 2-微客
	 * @param userId 用户id
	 * @return 佣金管理-佣金账户-认证
	 */
	DistributionCommissionAccountAuth getByIdentityTypeAndUserId(Integer identityType, Long userId);


	void auth(DistributionCommissionAccountAuthDTO commissionAccountAuthDTO);

	/**
	 * 保存佣金管理-佣金账户-认证
	 * @param distributionCommissionAccountAuth 佣金管理-佣金账户-认证
	 */
	void save(DistributionCommissionAccountAuth distributionCommissionAccountAuth);

	/**
	 * 更新佣金管理-佣金账户-认证
	 * @param distributionCommissionAccountAuth 佣金管理-佣金账户-认证
	 */
	void update(DistributionCommissionAccountAuth distributionCommissionAccountAuth);

}
