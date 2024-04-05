package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionWithdrawProcessLog;

/**
 * 佣金处理批次记录
 *
 * @author ZengFanChang
 * @date 2021-12-10 22:02:49
 */
public interface DistributionWithdrawProcessLogService {

	/**
	 * 分页获取佣金处理批次记录列表
	 * @param pageDTO 分页参数
	 * @return 佣金处理批次记录列表分页数据
	 */
	PageVO<DistributionWithdrawProcessLog> page(PageDTO pageDTO);

	/**
	 * 根据佣金处理批次记录id获取佣金处理批次记录
	 *
	 * @param id 佣金处理批次记录id
	 * @return 佣金处理批次记录
	 */
	DistributionWithdrawProcessLog getById(Long id);

	/**
	 * 保存佣金处理批次记录
	 * @param distributionWithdrawProcessLog 佣金处理批次记录
	 */
	void save(DistributionWithdrawProcessLog distributionWithdrawProcessLog);

	/**
	 * 更新佣金处理批次记录
	 * @param distributionWithdrawProcessLog 佣金处理批次记录
	 */
	void update(DistributionWithdrawProcessLog distributionWithdrawProcessLog);

	/**
	 * 根据佣金处理批次记录id删除佣金处理批次记录
	 * @param id 佣金处理批次记录id
	 */
	void deleteById(Long id);
}
