package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.DistributionGuideShareRecordDTO;
import com.mall4j.cloud.distribution.model.DistributionGuideShareRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 分销数据-导购分销效果
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
public interface DistributionGuideShareRecordMapper {

	/**
	 * 获取分销数据-导购分销效果列表
	 * @return 分销数据-导购分销效果列表
	 */
	List<DistributionGuideShareRecord> list(@Param("distributionGuideShareRecord") DistributionGuideShareRecordDTO dto);

	/**
	 * 根据分销数据-导购分销效果id获取分销数据-导购分销效果
	 *
	 * @param id 分销数据-导购分销效果id
	 * @return 分销数据-导购分销效果
	 */
	DistributionGuideShareRecord getById(@Param("id") Long id);

	/**
	 * 保存分销数据-导购分销效果
	 * @param distributionGuideShareRecord 分销数据-导购分销效果
	 */
	void save(@Param("distributionGuideShareRecord") DistributionGuideShareRecord distributionGuideShareRecord);

	/**
	 * 更新分销数据-导购分销效果
	 * @param distributionGuideShareRecord 分销数据-导购分销效果
	 */
	void update(@Param("distributionGuideShareRecord") DistributionGuideShareRecord distributionGuideShareRecord);

	/**
	 * 根据分销数据-导购分销效果id删除分销数据-导购分销效果
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

    DistributionGuideShareRecord getByGuideAndActivityAndDate(@Param("guideId") Long guideId, @Param("activityType") Integer activityType, @Param("date") Date date);

	Integer countStaffNum(@Param("distributionGuideShareRecord") DistributionGuideShareRecordDTO dto);
}
