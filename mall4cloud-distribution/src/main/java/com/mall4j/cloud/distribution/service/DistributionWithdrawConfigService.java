package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionWithdrawConfig;

/**
 * 佣金管理-佣金提现配置
 *
 * @author ZengFanChang
 * @date 2021-12-05 19:21:36
 */
public interface DistributionWithdrawConfigService {

	/**
	 * 分页获取佣金管理-佣金提现配置列表
	 * @param pageDTO 分页参数
	 * @return 佣金管理-佣金提现配置列表分页数据
	 */
	PageVO<DistributionWithdrawConfig> page(PageDTO pageDTO);

	/**
	 * 根据佣金管理-佣金提现配置id获取佣金管理-佣金提现配置
	 *
	 * @param id 佣金管理-佣金提现配置id
	 * @return 佣金管理-佣金提现配置
	 */
	DistributionWithdrawConfig getById(Long id);

	/**
	 * 保存佣金管理-佣金提现配置
	 * @param distributionWithdrawConfig 佣金管理-佣金提现配置
	 */
	void save(DistributionWithdrawConfig distributionWithdrawConfig);

	/**
	 * 更新佣金管理-佣金提现配置
	 * @param distributionWithdrawConfig 佣金管理-佣金提现配置
	 */
	void update(DistributionWithdrawConfig distributionWithdrawConfig);

	/**
	 * 根据佣金管理-佣金提现配置id删除佣金管理-佣金提现配置
	 * @param id 佣金管理-佣金提现配置id
	 */
	void deleteById(Long id);

	DistributionWithdrawConfig getByIdentityType(Integer identityType);

}
