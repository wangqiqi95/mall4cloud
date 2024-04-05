package com.mall4j.cloud.group.service;

import com.mall4j.cloud.api.group.feign.dto.TimeDiscountActivityDTO;
import com.mall4j.cloud.api.group.feign.vo.TimeDiscountActivityVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
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
public interface TimeLimitedDiscountActivityService {

	/**
	 * 分页获取限时调价活动列表
	 * @param pageDTO 分页参数
	 * @return 限时调价活动列表分页数据
	 */
	PageVO<TimeLimitedDiscountActivityPageVO> page(PageDTO pageDTO, TimeLimitDiscountActivityPageDTO param);

	/**
	 * 根据限时调价活动id获取限时调价活动
	 *
	 * @param id 限时调价活动id
	 * @return 限时调价活动
	 */
    TimeLimitedDiscountActivityVO getById(Long id);

	/**
	 * 保存限时调价活动
	 * @param timeLimitedDiscountActivity 限时调价活动
	 */
	void save(TimeLimitedDiscountActivity timeLimitedDiscountActivity);

	/**
	 * 更新限时调价活动
	 * @param timeLimitedDiscountActivity 限时调价活动
	 */
	void update(TimeLimitedDiscountActivity timeLimitedDiscountActivity);

	/**
	 * 根据限时调价活动id删除限时调价活动
	 * @param id 限时调价活动id
	 */
	void deleteById(Long id);

	void removeCache(Long id);

	void updateCheckStatusBatch(List<TimeLimitDiscountActivityCheckDTO> checkDTOS);


	 List<TimeDiscountActivityVO> convertActivityPrice(TimeDiscountActivityDTO params);

	 List<TimeDiscountActivityVO> convertActivityPricesNoFilter(TimeDiscountActivityDTO params);

	List<TimeDiscountActivityVO> currentActivityBySpuId(TimeDiscountActivityDTO params);

	List<TimeDiscountActivityVO> selectBySkuIds(List<Integer> activityids, List<Long> skuIds);
}
