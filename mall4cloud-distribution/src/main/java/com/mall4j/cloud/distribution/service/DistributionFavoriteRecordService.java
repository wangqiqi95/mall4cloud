package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionFavoriteRecord;

/**
 * 分享推广-收藏记录
 *
 * @author gww
 * @date 2022-01-30 02:23:13
 */
public interface DistributionFavoriteRecordService {

	/**
	 * 分页获取分享推广-收藏记录列表
	 * @param pageDTO 分页参数
	 * @return 分享推广-收藏记录列表分页数据
	 */
	PageVO<DistributionFavoriteRecord> page(PageDTO pageDTO);


	/**
	 * 保存分享推广-收藏记录
	 * @param distributionFavoriteRecord 分享推广-收藏记录
	 */
	void save(DistributionFavoriteRecord distributionFavoriteRecord);

}
