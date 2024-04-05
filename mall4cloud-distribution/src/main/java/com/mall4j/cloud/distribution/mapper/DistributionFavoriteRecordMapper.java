package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionFavoriteRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分享推广-收藏记录
 *
 * @author gww
 * @date 2022-01-30 02:23:13
 */
public interface DistributionFavoriteRecordMapper {

	/**
	 * 获取分享推广-收藏记录列表
	 * @return 分享推广-收藏记录列表
	 */
	List<DistributionFavoriteRecord> list();


	/**
	 * 保存分享推广-收藏记录
	 * @param distributionFavoriteRecord 分享推广-收藏记录
	 */
	void save(@Param("distributionFavoriteRecord") DistributionFavoriteRecord distributionFavoriteRecord);

}
