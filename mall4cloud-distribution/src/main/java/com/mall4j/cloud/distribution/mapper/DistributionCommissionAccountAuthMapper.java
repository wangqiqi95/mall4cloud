package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionCommissionAccountAuth;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 佣金管理-佣金账户-认证
 *
 * @author gww
 * @date 2022-01-31 12:15:41
 */
public interface DistributionCommissionAccountAuthMapper {


	/**
	 * 通过身份类型和用户id查询佣金账户认证信息
	 *
	 * @param  identityType 身份类型 1-导购 2-微客
	 * @param  userId 用户ID
	 * @return
	 */
	DistributionCommissionAccountAuth getByIdentityTypeAndUserId(@Param("identityType") Integer identityType, @Param("userId") Long userId);

	/**
	 * 保存佣金管理-佣金账户-认证
	 * @param distributionCommissionAccountAuth 佣金管理-佣金账户-认证
	 */
	void save(@Param("distributionCommissionAccountAuth") DistributionCommissionAccountAuth distributionCommissionAccountAuth);

	/**
	 * 更新佣金管理-佣金账户-认证
	 * @param distributionCommissionAccountAuth 佣金管理-佣金账户-认证
	 */
	void update(@Param("distributionCommissionAccountAuth") DistributionCommissionAccountAuth distributionCommissionAccountAuth);

}
