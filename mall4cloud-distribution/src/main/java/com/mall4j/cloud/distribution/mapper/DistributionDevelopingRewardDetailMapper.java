package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionDevelopingRewardDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销推广-发展奖励明细
 *
 * @author ZengFanChang
 * @date 2021-12-26 17:16:08
 */
public interface DistributionDevelopingRewardDetailMapper {

	/**
	 * 获取分销推广-发展奖励明细列表
	 * @return 分销推广-发展奖励明细列表
	 */
	List<DistributionDevelopingRewardDetail> list();

	/**
	 * 根据分销推广-发展奖励明细id获取分销推广-发展奖励明细
	 *
	 * @param id 分销推广-发展奖励明细id
	 * @return 分销推广-发展奖励明细
	 */
	DistributionDevelopingRewardDetail getById(@Param("id") Long id);

	/**
	 * 保存分销推广-发展奖励明细
	 * @param distributionDevelopingRewardDetail 分销推广-发展奖励明细
	 */
	void save(@Param("distributionDevelopingRewardDetail") DistributionDevelopingRewardDetail distributionDevelopingRewardDetail);

	/**
	 * 更新分销推广-发展奖励明细
	 * @param distributionDevelopingRewardDetail 分销推广-发展奖励明细
	 */
	void update(@Param("distributionDevelopingRewardDetail") DistributionDevelopingRewardDetail distributionDevelopingRewardDetail);

	/**
	 * 根据分销推广-发展奖励明细id删除分销推广-发展奖励明细
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

    DistributionDevelopingRewardDetail getByRewardIdAndStaffId(@Param("rewardId") Long rewardId, @Param("staffId") Long staffId);
}
