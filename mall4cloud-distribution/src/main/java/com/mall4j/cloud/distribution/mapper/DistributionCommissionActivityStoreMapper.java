package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionCommissionActivityStore;
import com.mall4j.cloud.distribution.vo.DistributionCommissionActivityCountVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 佣金配置-活动佣金-门店
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
public interface DistributionCommissionActivityStoreMapper {

	/**
	 * 通过活动id查询适用门店集合
	 * @param activityId
	 * @return 佣金配置-活动佣金-门店列表
	 */
	List<DistributionCommissionActivityStore> listByActivityId(@Param("activityId") Long activityId);

	/**
	 * 通过门店id查询适用门店集合
	 * @param storeId
	 * @return 佣金配置-活动佣金-门店列表
	 */
	List<DistributionCommissionActivityStore> listByStoreId(@Param("storeId") Long storeId);

	/**
	 * 保存佣金配置-活动佣金-门店
	 * @param distributionCommissionActivityStore 佣金配置-活动佣金-门店
	 */
	void save(@Param("distributionCommissionActivityStore") DistributionCommissionActivityStore distributionCommissionActivityStore);

	/**
	 * 根据佣金配置-活动佣金-门店id删除佣金配置-活动佣金-门店
	 * @param storeId
	 */
	void deleteByActivityIdAndStoreId(@Param("activityId") Long activityId, @Param("storeId") Long storeId);

	/**
	 * 根据佣金活动ID删除佣金配置-活动佣金-门店
	 * @param activityId
	 */
	void deleteByActivityId(@Param("activityId") Long activityId);

	/**
	 * 批量保存-活动佣金-门店
	 *
	 * @param activityStoreList
	 */
	void saveBatch(@Param("activityStoreList") List<DistributionCommissionActivityStore> activityStoreList);

	/**
	 * 分组统计活动下门店
	 *
	 * @return
	 */
	List<DistributionCommissionActivityCountVO> countByActivityIdList(@Param("activityIdList") List<Long> activityIdList);

}
