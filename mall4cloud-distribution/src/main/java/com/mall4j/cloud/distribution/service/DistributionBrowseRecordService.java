package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionBrowseRecordDTO;
import com.mall4j.cloud.distribution.model.DistributionBrowseRecord;
import com.mall4j.cloud.distribution.vo.DistributionBrowseRecordVO;
import com.mall4j.cloud.distribution.vo.DistributionPromotionStatVO;

import java.util.Date;
import java.util.List;

/**
 * 分享推广-浏览记录
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
public interface DistributionBrowseRecordService {

	/**
	 * 分页获取分享推广-浏览记录列表
	 * @param pageDTO 分页参数
	 * @return 分享推广-浏览记录列表分页数据
	 */
	PageVO<DistributionBrowseRecordVO> page(PageDTO pageDTO, DistributionBrowseRecordDTO dto);

	/**
	 * 根据分享推广-浏览记录id获取分享推广-浏览记录
	 *
	 * @param id 分享推广-浏览记录id
	 * @return 分享推广-浏览记录
	 */
	DistributionBrowseRecord getById(Long id);

	/**
	 * 保存分享推广-浏览记录
	 * @param distributionBrowseRecord 分享推广-浏览记录
	 */
	void save(DistributionBrowseRecord distributionBrowseRecord);

	/**
	 * 更新分享推广-浏览记录
	 * @param distributionBrowseRecord 分享推广-浏览记录
	 */
	void update(DistributionBrowseRecord distributionBrowseRecord);

	/**
	 * 根据分享推广-浏览记录id删除分享推广-浏览记录
	 * @param id 分享推广-浏览记录id
	 */
	void deleteById(Long id);

	Integer countNumByActivityAndDate(Long activityId, Integer activityType, Date startDate, Date endDate);

	Integer countNumByActivityAndDate(Long shareId, Long activityId, Integer activityType, Date startDate, Date endDate);

	Integer countUserNumByActivityAndDate(Long activityId, Integer activityType, Date startDate, Date endDate);

	Integer countUserNumByActivityAndDate(Long shareId, Long activityId, Integer activityType, Date startDate, Date endDate);

	/**
	 * 分享推广-浏览记录统计
	 *
	 * @param shareType 类型 1 导购 2微客
	 * @param shareId 分享人ID
	 * @return
	 */
	DistributionPromotionStatVO stat(Integer shareType, Long shareId);

	List<DistributionBrowseRecordVO> listStaffByActivity(Long activityId, Integer activityType, Date startDate, Date endDate);

}
