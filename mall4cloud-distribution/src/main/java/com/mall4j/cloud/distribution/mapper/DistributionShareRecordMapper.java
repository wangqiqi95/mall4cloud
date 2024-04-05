package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionShareRecord;
import com.mall4j.cloud.distribution.vo.DistributionShareRecordVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 分享推广-分享记录
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
public interface DistributionShareRecordMapper {

	/**
	 * 获取分享推广-分享记录列表
	 * @return 分享推广-分享记录列表
	 */
	List<DistributionShareRecord> list();

	/**
	 * 根据分享推广-分享记录id获取分享推广-分享记录
	 *
	 * @param id 分享推广-分享记录id
	 * @return 分享推广-分享记录
	 */
	DistributionShareRecord getById(@Param("id") Long id);

	/**
	 * 保存分享推广-分享记录
	 * @param distributionShareRecord 分享推广-分享记录
	 */
	void save(@Param("distributionShareRecord") DistributionShareRecord distributionShareRecord);

	/**
	 * 更新分享推广-分享记录
	 * @param distributionShareRecord 分享推广-分享记录
	 */
	void update(@Param("distributionShareRecord") DistributionShareRecord distributionShareRecord);

	/**
	 * 根据分享推广-分享记录id删除分享推广-分享记录
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

    int countByActivityAndDate(@Param("activityId") Long activityId, @Param("activityType") Integer activityType, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    int countByShareActivityAndDate(@Param("shareId")Long shareId, @Param("activityId") Long activityId, @Param("activityType") Integer activityType, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<DistributionShareRecordVO> listStaffByActivity(@Param("activityId") Long activityId, @Param("activityType") Integer activityType, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
