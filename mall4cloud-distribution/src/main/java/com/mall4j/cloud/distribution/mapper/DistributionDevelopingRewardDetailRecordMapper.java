package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionDevelopingRewardDetailRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销推广-发展奖励发展明细
 *
 * @author ZengFanChang
 * @date 2021-12-26 21:39:02
 */
public interface DistributionDevelopingRewardDetailRecordMapper {

	/**
	 * 获取分销推广-发展奖励发展明细列表
	 * @return 分销推广-发展奖励发展明细列表
	 */
	List<DistributionDevelopingRewardDetailRecord> list();

	/**
	 * 根据分销推广-发展奖励发展明细id获取分销推广-发展奖励发展明细
	 *
	 * @param id 分销推广-发展奖励发展明细id
	 * @return 分销推广-发展奖励发展明细
	 */
	DistributionDevelopingRewardDetailRecord getById(@Param("id") Long id);

	/**
	 * 保存分销推广-发展奖励发展明细
	 * @param distributionDevelopingRewardDetailRecord 分销推广-发展奖励发展明细
	 */
	void save(@Param("distributionDevelopingRewardDetailRecord") DistributionDevelopingRewardDetailRecord distributionDevelopingRewardDetailRecord);

	/**
	 * 更新分销推广-发展奖励发展明细
	 * @param distributionDevelopingRewardDetailRecord 分销推广-发展奖励发展明细
	 */
	void update(@Param("distributionDevelopingRewardDetailRecord") DistributionDevelopingRewardDetailRecord distributionDevelopingRewardDetailRecord);

	/**
	 * 根据分销推广-发展奖励发展明细id删除分销推广-发展奖励发展明细
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	DistributionDevelopingRewardDetailRecord getByUserId(@Param("userId") Long userId);

    int countRewardDetailRecordByRewardAndDetail(@Param("rewardId") Long rewardId, @Param("detailId") Long detailId);
}
