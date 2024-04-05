package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.CommissionActivityStoreSearchDTO;
import com.mall4j.cloud.distribution.dto.DistributionCommissionActivityStoreDTO;

import java.util.List;

/**
 * 佣金配置-活动佣金-门店
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
public interface DistributionCommissionActivityStoreService {


	/**
	 * 通过活动id查询适用门店id集合
	 *
	 * @param activityId
	 * @return
	 */
	List<Long> findStoreIdListByActivityId(Long activityId);

	/**
	 * 佣金活动添加门店
	 * @param distributionCommissionActivityStoreDTO
	 */
	void save(DistributionCommissionActivityStoreDTO distributionCommissionActivityStoreDTO);

	/**
	 * 佣金活动删除门店
	 * @param activityId 活动id
	 * @param id 门店id
	 */
	void deleteByActivityIdAndStoreId(Long activityId, Long id);
}
