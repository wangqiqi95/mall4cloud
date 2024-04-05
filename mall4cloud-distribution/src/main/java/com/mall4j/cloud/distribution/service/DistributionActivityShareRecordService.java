package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionActivityShareRecordDTO;
import com.mall4j.cloud.distribution.model.DistributionActivityShareRecord;

import java.util.Date;

/**
 * 分销推广-活动分享效果
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
public interface DistributionActivityShareRecordService {

	/**
	 * 分页获取分销推广-活动分享效果列表
	 * @param pageDTO 分页参数
	 * @return 分销推广-活动分享效果列表分页数据
	 */
	PageVO<DistributionActivityShareRecord> page(PageDTO pageDTO, DistributionActivityShareRecordDTO dto);

	/**
	 * 根据分销推广-活动分享效果id获取分销推广-活动分享效果
	 *
	 * @param id 分销推广-活动分享效果id
	 * @return 分销推广-活动分享效果
	 */
	DistributionActivityShareRecord getById(Long id);

	/**
	 * 保存分销推广-活动分享效果
	 * @param distributionActivityShareRecord 分销推广-活动分享效果
	 */
	void save(DistributionActivityShareRecord distributionActivityShareRecord);

	/**
	 * 更新分销推广-活动分享效果
	 * @param distributionActivityShareRecord 分销推广-活动分享效果
	 */
	void update(DistributionActivityShareRecord distributionActivityShareRecord);

	/**
	 * 根据分销推广-活动分享效果id删除分销推广-活动分享效果
	 * @param id 分销推广-活动分享效果id
	 */
	void deleteById(Long id);

	DistributionActivityShareRecord getByActivity(Long activityId, Integer activityType);

    void exportActivityShareRecord(DistributionActivityShareRecordDTO dto);

	void exportActivityShareDetails(DistributionActivityShareRecordDTO dto);
}
