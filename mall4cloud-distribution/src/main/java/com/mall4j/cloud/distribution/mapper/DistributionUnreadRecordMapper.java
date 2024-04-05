package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionUnreadRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销推广-用户未读信息
 *
 * @author ZengFanChang
 * @date 2022-01-23 22:16:06
 */
public interface DistributionUnreadRecordMapper {

	/**
	 * 获取分销推广-用户未读信息列表
	 * @return 分销推广-用户未读信息列表
	 */
	List<DistributionUnreadRecord> list();

	/**
	 * 根据分销推广-用户未读信息id获取分销推广-用户未读信息
	 *
	 * @param id 分销推广-用户未读信息id
	 * @return 分销推广-用户未读信息
	 */
	DistributionUnreadRecord getById(@Param("id") Long id);

	/**
	 * 保存分销推广-用户未读信息
	 * @param distributionUnreadRecord 分销推广-用户未读信息
	 */
	void save(@Param("distributionUnreadRecord") DistributionUnreadRecord distributionUnreadRecord);

	/**
	 * 更新分销推广-用户未读信息
	 * @param distributionUnreadRecord 分销推广-用户未读信息
	 */
	void update(@Param("distributionUnreadRecord") DistributionUnreadRecord distributionUnreadRecord);

	/**
	 * 根据分销推广-用户未读信息id删除分销推广-用户未读信息
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	DistributionUnreadRecord getByUser(@Param("identityType") Integer identityType, @Param("userId") Long userId);
}
