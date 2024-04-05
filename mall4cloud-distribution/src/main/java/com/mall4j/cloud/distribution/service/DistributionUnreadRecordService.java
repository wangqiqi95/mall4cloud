package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionUnreadRecord;

/**
 * 分销推广-用户未读信息
 *
 * @author ZengFanChang
 * @date 2022-01-23 22:16:06
 */
public interface DistributionUnreadRecordService {

	/**
	 * 分页获取分销推广-用户未读信息列表
	 * @param pageDTO 分页参数
	 * @return 分销推广-用户未读信息列表分页数据
	 */
	PageVO<DistributionUnreadRecord> page(PageDTO pageDTO);

	/**
	 * 根据分销推广-用户未读信息id获取分销推广-用户未读信息
	 *
	 * @param id 分销推广-用户未读信息id
	 * @return 分销推广-用户未读信息
	 */
	DistributionUnreadRecord getById(Long id);

	/**
	 * 查询导购/威客未读分销记录
	 * @param identityType 身份 1导购 2微客
	 * @param userId 用户ID
	 * @return
	 */
	DistributionUnreadRecord getByUser(Integer identityType, Long userId);

	/**
	 * 保存分销推广-用户未读信息
	 * @param distributionUnreadRecord 分销推广-用户未读信息
	 */
	void save(DistributionUnreadRecord distributionUnreadRecord);

	/**
	 * 更新分销推广-用户未读信息
	 * @param distributionUnreadRecord 分销推广-用户未读信息
	 */
	void update(DistributionUnreadRecord distributionUnreadRecord);

	/**
	 * 根据分销推广-用户未读信息id删除分销推广-用户未读信息
	 * @param id 分销推广-用户未读信息id
	 */
	void deleteById(Long id);
}
