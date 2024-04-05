package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionShareRecord;
import com.mall4j.cloud.distribution.vo.DistributionShareRecordVO;

import java.util.Date;
import java.util.List;

/**
 * 分享推广-分享记录
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
public interface DistributionShareRecordService {

	/**
	 * 分页获取分享推广-分享记录列表
	 * @param pageDTO 分页参数
	 * @return 分享推广-分享记录列表分页数据
	 */
	PageVO<DistributionShareRecord> page(PageDTO pageDTO);

	/**
	 * 根据分享推广-分享记录id获取分享推广-分享记录
	 *
	 * @param id 分享推广-分享记录id
	 * @return 分享推广-分享记录
	 */
	DistributionShareRecord getById(Long id);

	/**
	 * 保存分享推广-分享记录
	 * @param distributionShareRecord 分享推广-分享记录
	 */
	void save(DistributionShareRecord distributionShareRecord);

	/**
	 * 更新分享推广-分享记录
	 * @param distributionShareRecord 分享推广-分享记录
	 */
	void update(DistributionShareRecord distributionShareRecord);

	/**
	 * 根据分享推广-分享记录id删除分享推广-分享记录
	 * @param id 分享推广-分享记录id
	 */
	void deleteById(Long id);

	int countByActivityAndDate(Long activityId, Integer activityType, Date startDate, Date endDate);

	int countByShareActivityAndDate(Long shareId, Long activityId, Integer activityType, Date startDate, Date endDate);


	List<DistributionShareRecordVO> listStaffByActivity(Long activityId, Integer activityType, Date startDate, Date endDate);

}
