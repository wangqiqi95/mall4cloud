package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionWithdrawProcessLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 佣金处理批次记录
 *
 * @author ZengFanChang
 * @date 2021-12-10 22:02:49
 */
public interface DistributionWithdrawProcessLogMapper {

	/**
	 * 获取佣金处理批次记录列表
	 * @return 佣金处理批次记录列表
	 */
	List<DistributionWithdrawProcessLog> list();

	/**
	 * 根据佣金处理批次记录id获取佣金处理批次记录
	 *
	 * @param id 佣金处理批次记录id
	 * @return 佣金处理批次记录
	 */
	DistributionWithdrawProcessLog getById(@Param("id") Long id);

	/**
	 * 保存佣金处理批次记录
	 * @param distributionWithdrawProcessLog 佣金处理批次记录
	 */
	void save(@Param("distributionWithdrawProcessLog") DistributionWithdrawProcessLog distributionWithdrawProcessLog);

	/**
	 * 更新佣金处理批次记录
	 * @param distributionWithdrawProcessLog 佣金处理批次记录
	 */
	void update(@Param("distributionWithdrawProcessLog") DistributionWithdrawProcessLog distributionWithdrawProcessLog);

	/**
	 * 根据佣金处理批次记录id删除佣金处理批次记录
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	List<DistributionWithdrawProcessLog> listByProcess(@Param("processId") Long processId);
}
