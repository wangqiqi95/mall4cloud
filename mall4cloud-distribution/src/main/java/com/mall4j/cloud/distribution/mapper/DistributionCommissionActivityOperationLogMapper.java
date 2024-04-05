package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionCommissionActivityOperationLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 佣金配置-活动佣金-操作日志
 *
 * @author gww
 * @date 2021-12-16 16:04:31
 */
public interface DistributionCommissionActivityOperationLogMapper {

	/**
	 * 获取佣金配置-活动佣金-操作日志列表
	 * @return 佣金配置-活动佣金-操作日志列表
	 */
	List<DistributionCommissionActivityOperationLog> listByActivityId(@Param("activityId") Long activityId);

	/**
	 * 保存佣金配置-活动佣金-操作日志
	 * @param distributionCommissionActivityOperationLog 佣金配置-活动佣金-操作日志
	 */
	void save(@Param("distributionCommissionActivityOperationLog") DistributionCommissionActivityOperationLog distributionCommissionActivityOperationLog);

}
