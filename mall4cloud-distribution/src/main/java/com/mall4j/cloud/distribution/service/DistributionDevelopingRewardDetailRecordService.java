package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionDevelopingRewardDetailRecord;

/**
 * 分销推广-发展奖励发展明细
 *
 * @author ZengFanChang
 * @date 2021-12-26 21:39:02
 */
public interface DistributionDevelopingRewardDetailRecordService {

	/**
	 * 分页获取分销推广-发展奖励发展明细列表
	 * @param pageDTO 分页参数
	 * @return 分销推广-发展奖励发展明细列表分页数据
	 */
	PageVO<DistributionDevelopingRewardDetailRecord> page(PageDTO pageDTO);

	/**
	 * 根据分销推广-发展奖励发展明细id获取分销推广-发展奖励发展明细
	 *
	 * @param id 分销推广-发展奖励发展明细id
	 * @return 分销推广-发展奖励发展明细
	 */
	DistributionDevelopingRewardDetailRecord getById(Long id);

	/**
	 * 保存分销推广-发展奖励发展明细
	 * @param distributionDevelopingRewardDetailRecord 分销推广-发展奖励发展明细
	 */
	void save(DistributionDevelopingRewardDetailRecord distributionDevelopingRewardDetailRecord);

	/**
	 * 更新分销推广-发展奖励发展明细
	 * @param distributionDevelopingRewardDetailRecord 分销推广-发展奖励发展明细
	 */
	void update(DistributionDevelopingRewardDetailRecord distributionDevelopingRewardDetailRecord);

	/**
	 * 根据分销推广-发展奖励发展明细id删除分销推广-发展奖励发展明细
	 * @param id 分销推广-发展奖励发展明细id
	 */
	void deleteById(Long id);

	DistributionDevelopingRewardDetailRecord getByUserId(Long userId);

	int countRewardDetailRecordByRewardAndDetail(Long rewardId, Long detailId);

}
