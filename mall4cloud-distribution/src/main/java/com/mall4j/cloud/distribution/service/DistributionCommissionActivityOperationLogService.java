package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionCommissionActivityOperationLog;

/**
 * 佣金配置-活动佣金-操作日志
 *
 * @author gww
 * @date 2021-12-16 16:04:31
 */
public interface DistributionCommissionActivityOperationLogService {

	/**
	 * 分页获取佣金配置-活动佣金-操作日志列表
	 * @param pageDTO 分页参数
	 * @param activityId 活动ID
	 * @return 佣金配置-活动佣金-操作日志列表分页数据
	 */
	PageVO<DistributionCommissionActivityOperationLog> page(PageDTO pageDTO, Long activityId);

}
