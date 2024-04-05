package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionDevelopingRewardDTO;
import com.mall4j.cloud.distribution.model.DistributionDevelopingReward;

import java.util.List;

/**
 * 分销推广-发展奖励
 *
 * @author ZengFanChang
 * @date 2021-12-26 17:16:08
 */
public interface DistributionDevelopingRewardService {

	/**
	 * 分页获取分销推广-发展奖励列表
	 * @param pageDTO 分页参数
	 * @return 分销推广-发展奖励列表分页数据
	 */
	PageVO<DistributionDevelopingReward> page(PageDTO pageDTO , DistributionDevelopingRewardDTO dto);

	/**
	 * 根据分销推广-发展奖励id获取分销推广-发展奖励
	 *
	 * @param id 分销推广-发展奖励id
	 * @return 分销推广-发展奖励
	 */
	DistributionDevelopingRewardDTO getById(Long id);

	/**
	 * 保存分销推广-发展奖励
	 * @param distributionDevelopingReward 分销推广-发展奖励
	 */
	void save(DistributionDevelopingRewardDTO distributionDevelopingReward);

	/**
	 * 更新分销推广-发展奖励
	 * @param distributionDevelopingReward 分销推广-发展奖励
	 */
	void update(DistributionDevelopingRewardDTO distributionDevelopingReward);

	/**
	 * 根据分销推广-发展奖励id删除分销推广-发展奖励
	 * @param id 分销推广-发展奖励id
	 */
	void deleteById(Long id);

	List<DistributionDevelopingReward> listEffectRewardByIds(List<Long> ids);

}
