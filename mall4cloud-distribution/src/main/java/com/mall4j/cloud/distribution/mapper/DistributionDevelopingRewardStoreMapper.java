package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionDevelopingRewardStore;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销推广-发展奖励门店
 *
 * @author ZengFanChang
 * @date 2021-12-26 17:38:24
 */
public interface DistributionDevelopingRewardStoreMapper {

	/**
	 * 获取分销推广-发展奖励门店列表
	 * @return 分销推广-发展奖励门店列表
	 */
	List<DistributionDevelopingRewardStore> list();

	/**
	 * 根据分销推广-发展奖励门店id获取分销推广-发展奖励门店
	 *
	 * @param id 分销推广-发展奖励门店id
	 * @return 分销推广-发展奖励门店
	 */
	DistributionDevelopingRewardStore getById(@Param("id") Long id);

	/**
	 * 保存分销推广-发展奖励门店
	 * @param distributionDevelopingRewardStore 分销推广-发展奖励门店
	 */
	void save(@Param("distributionDevelopingRewardStore") DistributionDevelopingRewardStore distributionDevelopingRewardStore);

	/**
	 * 更新分销推广-发展奖励门店
	 * @param distributionDevelopingRewardStore 分销推广-发展奖励门店
	 */
	void update(@Param("distributionDevelopingRewardStore") DistributionDevelopingRewardStore distributionDevelopingRewardStore);

	/**
	 * 根据分销推广-发展奖励门店id删除分销推广-发展奖励门店
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	List<DistributionDevelopingRewardStore> listByRewardId(@Param("rewardId") Long rewardId);

	List<DistributionDevelopingRewardStore> listByStoreId(@Param("storeId") Long storeId);

	void deleteByRewardIdNotInStoreIds(@Param("rewardId") Long rewardId, @Param("storeIds") List<Long> storeIds);

	void deleteByRewardId(@Param("rewardId") Long rewardId);
}
