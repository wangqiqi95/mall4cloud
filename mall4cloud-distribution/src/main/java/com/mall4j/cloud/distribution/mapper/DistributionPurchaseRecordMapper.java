package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.DistributionPurchaseRecordDTO;
import com.mall4j.cloud.distribution.dto.PurchaseRankingDTO;
import com.mall4j.cloud.distribution.model.DistributionPurchaseRecord;
import com.mall4j.cloud.distribution.vo.DistributionPromotionGroupVO;
import com.mall4j.cloud.distribution.vo.DistributionPurchaseRecordVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.Date;
import java.util.List;

/**
 * 分销推广-加购记录
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
public interface DistributionPurchaseRecordMapper {

	/**
	 * 获取分销推广-加购记录列表
	 * @return 分销推广-加购记录列表
	 */
	List<DistributionPurchaseRecordVO> list(@Param("distributionPurchaseRecord") DistributionPurchaseRecordDTO dto);

	/**
	 * 根据分销推广-加购记录id获取分销推广-加购记录
	 *
	 * @param id 分销推广-加购记录id
	 * @return 分销推广-加购记录
	 */
	DistributionPurchaseRecord getById(@Param("id") Long id);

	/**
	 * 保存分销推广-加购记录
	 * @param distributionPurchaseRecord 分销推广-加购记录
	 */
	void save(@Param("distributionPurchaseRecord") DistributionPurchaseRecord distributionPurchaseRecord);

	/**
	 * 更新分销推广-加购记录
	 * @param distributionPurchaseRecord 分销推广-加购记录
	 */
	void update(@Param("distributionPurchaseRecord") DistributionPurchaseRecord distributionPurchaseRecord);

	/**
	 * 根据分销推广-加购记录id删除分销推广-加购记录
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	DistributionPurchaseRecord getByPurchaseAndActivity(@Param("purchaseId") Long purchaseId, @Param("purchaseType") String purchaseType, @Param("activityId") Long activityId, @Param("activityType") Integer activityType);

	Integer countNumByActivityAndDate(@Param("activityId") Long activityId, @Param("activityType") Integer activityType, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	Integer countUserNumByActivityAndDate(@Param("activityId") Long activityId, @Param("activityType") Integer activityType, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	List<DistributionPromotionGroupVO> groupByActivityType(@Param("shareType") Integer shareType, @Param("shareId") Long shareId);

    List<PurchaseRankingDTO> pagePurchaseRanking(@Param("dto") DistributionPurchaseRecordDTO dto);

	Integer countNumByShareActivityAndDate(@Param("shareId") Long shareId, @Param("activityId") Long activityId, @Param("activityType") Integer activityType, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	Integer countUserNumByShareActivityAndDate(@Param("shareId") Long shareId, @Param("activityId") Long activityId, @Param("activityType") Integer activityType, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
