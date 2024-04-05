package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.DistributionWithdrawProcessDTO;
import com.mall4j.cloud.distribution.model.DistributionWithdrawProcess;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 佣金处理批次
 *
 * @author ZengFanChang
 * @date 2021-12-10 22:02:48
 */
public interface DistributionWithdrawProcessMapper {

	/**
	 * 获取佣金处理批次列表
	 * @return 佣金处理批次列表
	 */
	List<DistributionWithdrawProcess> list(@Param("distributionWithdrawProcessDTO") DistributionWithdrawProcessDTO distributionWithdrawProcessDTO);

	/**
	 * 根据佣金处理批次id获取佣金处理批次
	 *
	 * @param id 佣金处理批次id
	 * @return 佣金处理批次
	 */
	DistributionWithdrawProcess getById(@Param("id") Long id);

	/**
	 * 保存佣金处理批次
	 * @param distributionWithdrawProcess 佣金处理批次
	 */
	void save(@Param("distributionWithdrawProcess") DistributionWithdrawProcess distributionWithdrawProcess);

	/**
	 * 更新佣金处理批次
	 * @param distributionWithdrawProcess 佣金处理批次
	 */
	void update(@Param("distributionWithdrawProcess") DistributionWithdrawProcess distributionWithdrawProcess);

	/**
	 * 根据佣金处理批次id删除佣金处理批次
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
