package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionCommissionLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 佣金流水信息
 *
 * @author ZengFanChang
 * @date 2021-12-11 19:35:15
 */
public interface DistributionCommissionLogMapper {

	/**
	 * 获取佣金流水信息列表
	 * @return 佣金流水信息列表
	 */
	List<DistributionCommissionLog> list();

	/**
	 * 根据佣金流水信息id获取佣金流水信息
	 *
	 * @param id 佣金流水信息id
	 * @return 佣金流水信息
	 */
	DistributionCommissionLog getById(@Param("id") Long id);

	/**
	 * 保存佣金流水信息
	 * @param distributionCommissionLog 佣金流水信息
	 */
	void save(@Param("distributionCommissionLog") DistributionCommissionLog distributionCommissionLog);

	/**
	 * 更新佣金流水信息
	 * @param distributionCommissionLog 佣金流水信息
	 */
	void update(@Param("distributionCommissionLog") DistributionCommissionLog distributionCommissionLog);

	/**
	 * 根据佣金流水信息id删除佣金流水信息
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
