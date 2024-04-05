package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionDevelopingRewardDetail;

/**
 * 分销推广-发展奖励明细
 *
 * @author ZengFanChang
 * @date 2021-12-26 17:16:08
 */
public interface DistributionDevelopingRewardDetailService {

	/**
	 * 分页获取分销推广-发展奖励明细列表
	 * @param pageDTO 分页参数
	 * @return 分销推广-发展奖励明细列表分页数据
	 */
	PageVO<DistributionDevelopingRewardDetail> page(PageDTO pageDTO);

	/**
	 * 根据分销推广-发展奖励明细id获取分销推广-发展奖励明细
	 *
	 * @param id 分销推广-发展奖励明细id
	 * @return 分销推广-发展奖励明细
	 */
	DistributionDevelopingRewardDetail getById(Long id);

	/**
	 * 保存分销推广-发展奖励明细
	 * @param distributionDevelopingRewardDetail 分销推广-发展奖励明细
	 */
	void save(DistributionDevelopingRewardDetail distributionDevelopingRewardDetail);

	/**
	 * 更新分销推广-发展奖励明细
	 * @param distributionDevelopingRewardDetail 分销推广-发展奖励明细
	 */
	void update(DistributionDevelopingRewardDetail distributionDevelopingRewardDetail);

	/**
	 * 根据分销推广-发展奖励明细id删除分销推广-发展奖励明细
	 * @param id 分销推广-发展奖励明细id
	 */
	void deleteById(Long id);

	DistributionDevelopingRewardDetail getByRewardIdAndStaffId(Long rewardId, Long staffId);
}
