package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionCommissionLog;

/**
 * 佣金流水信息
 *
 * @author ZengFanChang
 * @date 2021-12-11 19:35:15
 */
public interface DistributionCommissionLogService {

	/**
	 * 分页获取佣金流水信息列表
	 * @param pageDTO 分页参数
	 * @return 佣金流水信息列表分页数据
	 */
	PageVO<DistributionCommissionLog> page(PageDTO pageDTO);

	/**
	 * 根据佣金流水信息id获取佣金流水信息
	 *
	 * @param id 佣金流水信息id
	 * @return 佣金流水信息
	 */
	DistributionCommissionLog getById(Long id);

	/**
	 * 保存佣金流水信息
	 * @param distributionCommissionLog 佣金流水信息
	 */
	void save(DistributionCommissionLog distributionCommissionLog);

	/**
	 * 更新佣金流水信息
	 * @param distributionCommissionLog 佣金流水信息
	 */
	void update(DistributionCommissionLog distributionCommissionLog);

	/**
	 * 根据佣金流水信息id删除佣金流水信息
	 * @param id 佣金流水信息id
	 */
	void deleteById(Long id);
}
