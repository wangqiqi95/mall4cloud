package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionGuideShareRecordDTO;
import com.mall4j.cloud.distribution.model.DistributionGuideShareRecord;

import java.util.Date;

/**
 * 分销数据-导购分销效果
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
public interface DistributionGuideShareRecordService {

	/**
	 * 分页获取分销数据-导购分销效果列表
	 * @param pageDTO 分页参数
	 * @return 分销数据-导购分销效果列表分页数据
	 */
	PageVO<DistributionGuideShareRecord> page(PageDTO pageDTO, DistributionGuideShareRecordDTO dto);

	/**
	 * 根据分销数据-导购分销效果id获取分销数据-导购分销效果
	 *
	 * @param id 分销数据-导购分销效果id
	 * @return 分销数据-导购分销效果
	 */
	DistributionGuideShareRecord getById(Long id);

	/**
	 * 保存分销数据-导购分销效果
	 * @param distributionGuideShareRecord 分销数据-导购分销效果
	 */
	void save(DistributionGuideShareRecord distributionGuideShareRecord);

	/**
	 * 更新分销数据-导购分销效果
	 * @param distributionGuideShareRecord 分销数据-导购分销效果
	 */
	void update(DistributionGuideShareRecord distributionGuideShareRecord);

	/**
	 * 根据分销数据-导购分销效果id删除分销数据-导购分销效果
	 * @param id 分销数据-导购分销效果id
	 */
	void deleteById(Long id);

	DistributionGuideShareRecord getByGuideAndActivityAndDate(Long guideId, Integer activityType, Date date);

	Integer countStaffNum(DistributionGuideShareRecordDTO dto);

    void exportGuideShareRecord(DistributionGuideShareRecordDTO dto);

}
