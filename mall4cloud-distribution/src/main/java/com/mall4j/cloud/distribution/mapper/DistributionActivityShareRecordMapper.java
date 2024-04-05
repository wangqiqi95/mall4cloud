package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.DistributionActivityShareRecordDTO;
import com.mall4j.cloud.distribution.model.DistributionActivityShareRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销推广-活动分享效果
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
public interface DistributionActivityShareRecordMapper {

	/**
	 * 获取分销推广-活动分享效果列表
	 * @return 分销推广-活动分享效果列表
	 */
	List<DistributionActivityShareRecord> list(@Param("distributionActivityShareRecord") DistributionActivityShareRecordDTO dto);

	/**
	 * 根据分销推广-活动分享效果id获取分销推广-活动分享效果
	 *
	 * @param id 分销推广-活动分享效果id
	 * @return 分销推广-活动分享效果
	 */
	DistributionActivityShareRecord getById(@Param("id") Long id);

	/**
	 * 保存分销推广-活动分享效果
	 * @param distributionActivityShareRecord 分销推广-活动分享效果
	 */
	void save(@Param("distributionActivityShareRecord") DistributionActivityShareRecord distributionActivityShareRecord);

	/**
	 * 更新分销推广-活动分享效果
	 * @param distributionActivityShareRecord 分销推广-活动分享效果
	 */
	void update(@Param("distributionActivityShareRecord") DistributionActivityShareRecord distributionActivityShareRecord);

	/**
	 * 根据分销推广-活动分享效果id删除分销推广-活动分享效果
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

    DistributionActivityShareRecord getByActivity(@Param("activityId") Long activityId, @Param("activityType") Integer activityType);
}
