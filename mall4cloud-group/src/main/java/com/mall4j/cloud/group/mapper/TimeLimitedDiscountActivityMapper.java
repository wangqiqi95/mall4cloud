package com.mall4j.cloud.group.mapper;

import com.mall4j.cloud.api.group.feign.vo.TimeDiscountActivityVO;
import com.mall4j.cloud.group.dto.TimeLimitDiscountActivityCheckDTO;
import com.mall4j.cloud.group.dto.TimeLimitDiscountActivityPageDTO;
import com.mall4j.cloud.group.model.TimeLimitedDiscountActivity;
import com.mall4j.cloud.group.vo.TimeLimitedDiscountActivityPageVO;
import com.mall4j.cloud.group.vo.TimeLimitedDiscountActivityVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 限时调价活动
 *
 * @author FrozenWatermelon
 * @date 2022-03-10 01:55:13
 */
public interface TimeLimitedDiscountActivityMapper {

	/**
	 * 获取限时调价活动列表
	 * @return 限时调价活动列表
	 */
	List<TimeLimitedDiscountActivityPageVO> list(TimeLimitDiscountActivityPageDTO param);

	/**
	 * 根据限时调价活动id获取限时调价活动
	 *
	 * @param id 限时调价活动id
	 * @return 限时调价活动
	 */
    TimeLimitedDiscountActivityVO getById(@Param("id") Long id);

	/**
	 * 保存限时调价活动
	 * @param timeLimitedDiscountActivity 限时调价活动
	 */
	void save(@Param("timeLimitedDiscountActivity") TimeLimitedDiscountActivity timeLimitedDiscountActivity);

	/**
	 * 更新限时调价活动
	 * @param timeLimitedDiscountActivity 限时调价活动
	 */
	void update(@Param("timeLimitedDiscountActivity") TimeLimitedDiscountActivity timeLimitedDiscountActivity);

	/**
	 * 根据限时调价活动id删除限时调价活动
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

    Integer enable(@Param("id") Long id);

    Integer disable(@Param("id") Long id);

    List<TimeLimitedDiscountActivityVO> currentActivity(@Param("storeId")Long storeId,
														@Param("checkStatus")Integer checkStatus,
														@Param("type")Integer type);

	List<TimeDiscountActivityVO> currentActivityBySpuId(@Param("storeId")Long storeId,
														 @Param("spuIds")List spuId);

	void updateCheckStatusBatch(@Param("checkDTOS")List<TimeLimitDiscountActivityCheckDTO> checkDTOS);
}
