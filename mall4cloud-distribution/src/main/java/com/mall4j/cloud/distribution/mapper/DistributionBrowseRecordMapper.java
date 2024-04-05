package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.DistributionBrowseRecordDTO;
import com.mall4j.cloud.distribution.model.DistributionBrowseRecord;
import com.mall4j.cloud.distribution.vo.DistributionBrowseRecordVO;
import com.mall4j.cloud.distribution.vo.DistributionPromotionGroupVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 分享推广-浏览记录
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
public interface DistributionBrowseRecordMapper {

	/**
	 * 获取分享推广-浏览记录列表
	 * @return 分享推广-浏览记录列表
	 */
	List<DistributionBrowseRecordVO> list(@Param("distributionBrowseRecord") DistributionBrowseRecordDTO dto);

	/**
	 * 根据分享推广-浏览记录id获取分享推广-浏览记录
	 *
	 * @param id 分享推广-浏览记录id
	 * @return 分享推广-浏览记录
	 */
	DistributionBrowseRecord getById(@Param("id") Long id);

	/**
	 * 保存分享推广-浏览记录
	 * @param distributionBrowseRecord 分享推广-浏览记录
	 */
	void save(@Param("distributionBrowseRecord") DistributionBrowseRecord distributionBrowseRecord);

	/**
	 * 更新分享推广-浏览记录
	 * @param distributionBrowseRecord 分享推广-浏览记录
	 */
	void update(@Param("distributionBrowseRecord") DistributionBrowseRecord distributionBrowseRecord);

	/**
	 * 根据分享推广-浏览记录id删除分享推广-浏览记录
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	Integer countByBrowseAndActivity(@Param("browseId") Long browseId, @Param("browseType") Integer browseType, @Param("activityId") Long activityId, @Param("activityType") Integer activityType);

	Integer countNumByActivityAndDate(@Param("activityId") Long activityId, @Param("activityType") Integer activityType, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	Integer countNumByShareActivityAndDate(@Param("shareId") Long shareId, @Param("activityId") Long activityId, @Param("activityType") Integer activityType, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	Integer countUserNumByActivityAndDate(@Param("activityId") Long activityId, @Param("activityType") Integer activityType, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	Integer countUserNumByShareActivityAndDate(@Param("shareId") Long shareId, @Param("activityId") Long activityId, @Param("activityType") Integer activityType, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	List<DistributionPromotionGroupVO> groupByActivityType(@Param("shareType") Integer shareType, @Param("shareId") Long shareId);

	List<DistributionBrowseRecordVO> listStaffByActivity(@Param("activityId") Long activityId, @Param("activityType") Integer activityType, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
