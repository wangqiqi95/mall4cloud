package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionBuyRecordDTO;
import com.mall4j.cloud.distribution.model.DistributionBuyRecord;
import com.mall4j.cloud.distribution.vo.DistributionBuyRecordVO;
import com.mall4j.cloud.distribution.vo.DistributionPromotionStatVO;

import java.util.Date;

/**
 * 分销推广-下单记录
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
public interface DistributionBuyRecordService {

	/**
	 * 分页获取分销推广-下单记录列表
	 * @param pageDTO 分页参数
	 * @return 分销推广-下单记录列表分页数据
	 */
	PageVO<DistributionBuyRecordVO> page(PageDTO pageDTO, DistributionBuyRecordDTO dto);

	/**
	 * 根据分销推广-下单记录id获取分销推广-下单记录
	 *
	 * @param id 分销推广-下单记录id
	 * @return 分销推广-下单记录
	 */
	DistributionBuyRecord getById(Long id);

	/**
	 * 保存分销推广-下单记录
	 * @param distributionBuyRecord 分销推广-下单记录
	 */
	void save(DistributionBuyRecord distributionBuyRecord);

	/**
	 * 更新分销推广-下单记录
	 * @param distributionBuyRecord 分销推广-下单记录
	 */
	void update(DistributionBuyRecord distributionBuyRecord);

	/**
	 * 根据分销推广-下单记录id删除分销推广-下单记录
	 * @param id 分销推广-下单记录id
	 */
	void deleteById(Long id);

	int countNumByActivityAndDate(Long activityId, Integer activityType, Date startDate, Date endDate);

	int countUserNumByActivityAndDate(Long activityId, Integer activityType, Date startDate, Date endDate);

	/**
	 * 分享推广-下单记录统计
	 *
	 * @param shareType 类型 1 导购 2微客
	 * @param shareId 分享人ID
	 * @return
	 */
	DistributionPromotionStatVO stat(Integer shareType, Long shareId);

}
