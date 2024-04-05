package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionPurchaseRecordDTO;
import com.mall4j.cloud.distribution.dto.PurchaseRankingDTO;
import com.mall4j.cloud.distribution.model.DistributionPurchaseRecord;
import com.mall4j.cloud.distribution.vo.DistributionPromotionStatVO;
import com.mall4j.cloud.distribution.vo.DistributionPurchaseRecordVO;

import java.util.Date;

/**
 * 分销推广-加购记录
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
public interface DistributionPurchaseRecordService {

	/**
	 * 分页获取分销推广-加购记录列表
	 * @param pageDTO 分页参数
	 * @return 分销推广-加购记录列表分页数据
	 */
	PageVO<DistributionPurchaseRecordVO> page(PageDTO pageDTO, DistributionPurchaseRecordDTO dto);

	/**
	 * 根据分销推广-加购记录id获取分销推广-加购记录
	 *
	 * @param id 分销推广-加购记录id
	 * @return 分销推广-加购记录
	 */
	DistributionPurchaseRecord getById(Long id);

	/**
	 * 保存分销推广-加购记录
	 * @param distributionPurchaseRecord 分销推广-加购记录
	 */
	void save(DistributionPurchaseRecord distributionPurchaseRecord);

	/**
	 * 更新分销推广-加购记录
	 * @param distributionPurchaseRecord 分销推广-加购记录
	 */
	void update(DistributionPurchaseRecord distributionPurchaseRecord);

	/**
	 * 根据分销推广-加购记录id删除分销推广-加购记录
	 * @param id 分销推广-加购记录id
	 */
	void deleteById(Long id);

	Integer countNumByActivityAndDate(Long activityId, Integer activityType, Date startDate, Date endDate);

	Integer countNumByShareActivityAndDate(Long shareId, Long activityId, Integer activityType, Date startDate, Date endDate);

	Integer countUserNumByActivityAndDate(Long activityId, Integer activityType, Date startDate, Date endDate);

	Integer countUserNumByShareActivityAndDate(Long shareId, Long activityId, Integer activityType, Date startDate, Date endDate);

	/**
	 * 分享推广-加购记录统计
	 *
	 * @param shareType 类型 1 导购 2微客
	 * @param shareId 分享人ID
	 * @return
	 */
	DistributionPromotionStatVO stat(Integer shareType, Long shareId);

	PageVO<PurchaseRankingDTO> pagePurchaseRanking(PageDTO pageDTO, DistributionPurchaseRecordDTO dto);
}
