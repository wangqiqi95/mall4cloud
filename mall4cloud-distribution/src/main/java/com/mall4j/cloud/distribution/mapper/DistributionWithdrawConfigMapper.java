package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionWithdrawConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 佣金管理-佣金提现配置
 *
 * @author ZengFanChang
 * @date 2021-12-05 19:21:36
 */
public interface DistributionWithdrawConfigMapper {

	/**
	 * 获取佣金管理-佣金提现配置列表
	 * @return 佣金管理-佣金提现配置列表
	 */
	List<DistributionWithdrawConfig> list();

	/**
	 * 根据佣金管理-佣金提现配置id获取佣金管理-佣金提现配置
	 *
	 * @param id 佣金管理-佣金提现配置id
	 * @return 佣金管理-佣金提现配置
	 */
	DistributionWithdrawConfig getById(@Param("id") Long id);

	/**
	 * 保存佣金管理-佣金提现配置
	 * @param distributionWithdrawConfig 佣金管理-佣金提现配置
	 */
	void save(@Param("distributionWithdrawConfig") DistributionWithdrawConfig distributionWithdrawConfig);

	/**
	 * 更新佣金管理-佣金提现配置
	 * @param distributionWithdrawConfig 佣金管理-佣金提现配置
	 */
	void update(@Param("distributionWithdrawConfig") DistributionWithdrawConfig distributionWithdrawConfig);

	/**
	 * 根据佣金管理-佣金提现配置id删除佣金管理-佣金提现配置
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

    DistributionWithdrawConfig getByIdentityType(@Param("identityType") Integer identityType);
}
