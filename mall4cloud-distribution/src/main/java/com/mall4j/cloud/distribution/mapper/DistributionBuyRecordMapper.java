package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.DistributionBuyRecordDTO;
import com.mall4j.cloud.distribution.model.DistributionBuyRecord;
import com.mall4j.cloud.distribution.vo.DistributionBuyRecordVO;
import com.mall4j.cloud.distribution.vo.DistributionPromotionGroupVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.Date;
import java.util.List;

/**
 * 分销推广-下单记录
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
public interface DistributionBuyRecordMapper {

	/**
	 * 获取分销推广-下单记录列表
	 * @return 分销推广-下单记录列表
	 */
	List<DistributionBuyRecordVO> list(@Param("distributionBuyRecord") DistributionBuyRecordDTO dto);

	/**
	 * 根据分销推广-下单记录id获取分销推广-下单记录
	 *
	 * @param id 分销推广-下单记录id
	 * @return 分销推广-下单记录
	 */
	DistributionBuyRecord getById(@Param("id") Long id);

	/**
	 * 保存分销推广-下单记录
	 * @param distributionBuyRecord 分销推广-下单记录
	 */
	void save(@Param("distributionBuyRecord") DistributionBuyRecord distributionBuyRecord);

	/**
	 * 更新分销推广-下单记录
	 * @param distributionBuyRecord 分销推广-下单记录
	 */
	void update(@Param("distributionBuyRecord") DistributionBuyRecord distributionBuyRecord);

	/**
	 * 根据分销推广-下单记录id删除分销推广-下单记录
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	DistributionBuyRecord getByBuyAndActivity(@Param("buyId") Long buyId, @Param("buyType") Integer buyType, @Param("activityId") Long activityId, @Param("activityType") Integer activityType);

	int countNumByActivityAndDate(@Param("activityId") Long activityId, @Param("activityType") Integer activityType, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	int countUserNumByActivityAndDate(@Param("activityId") Long activityId, @Param("activityType") Integer activityType, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	List<DistributionPromotionGroupVO> groupByActivityType(@Param("shareType") Integer shareType, @Param("shareId") Long shareId);
}
