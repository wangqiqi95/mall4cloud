package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.DistributionDevelopingRewardDTO;
import com.mall4j.cloud.distribution.model.DistributionDevelopingReward;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销推广-发展奖励
 *
 * @author ZengFanChang
 * @date 2021-12-26 17:16:08
 */
public interface DistributionDevelopingRewardMapper {

	/**
	 * 获取分销推广-发展奖励列表
	 * @return 分销推广-发展奖励列表
	 */
	List<DistributionDevelopingReward> list(@Param("distributionDevelopingReward") DistributionDevelopingRewardDTO dto);

	/**
	 * 根据分销推广-发展奖励id获取分销推广-发展奖励
	 *
	 * @param id 分销推广-发展奖励id
	 * @return 分销推广-发展奖励
	 */
	DistributionDevelopingReward getById(@Param("id") Long id);

	/**
	 * 保存分销推广-发展奖励
	 * @param distributionDevelopingReward 分销推广-发展奖励
	 */
	void save(@Param("distributionDevelopingReward") DistributionDevelopingReward distributionDevelopingReward);

	/**
	 * 更新分销推广-发展奖励
	 * @param distributionDevelopingReward 分销推广-发展奖励
	 */
	void update(@Param("distributionDevelopingReward") DistributionDevelopingReward distributionDevelopingReward);

	/**
	 * 根据分销推广-发展奖励id删除分销推广-发展奖励
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	List<DistributionDevelopingReward> listEffectRewardByIds(@Param("ids") List<Long> ids);

}
