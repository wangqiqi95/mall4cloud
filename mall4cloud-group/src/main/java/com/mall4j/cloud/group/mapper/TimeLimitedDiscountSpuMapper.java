package com.mall4j.cloud.group.mapper;

import com.mall4j.cloud.group.model.TimeLimitedDiscountSpu;
import com.mall4j.cloud.group.vo.TimeLimitedDiscountSpuVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 限时调价活动 spu价格
 *
 * @author FrozenWatermelon
 * @date 2022-03-10 13:29:18
 */
public interface TimeLimitedDiscountSpuMapper {

	/**
	 * 获取限时调价活动 spu价格列表
	 * @return 限时调价活动 spu价格列表
	 */
	List<TimeLimitedDiscountSpu> list();

	/**
	 * 根据限时调价活动 spu价格id获取限时调价活动 spu价格
	 *
	 * @param id 限时调价活动 spu价格id
	 * @return 限时调价活动 spu价格
	 */
	TimeLimitedDiscountSpu getById(@Param("id") Long id);

	/**
	 * 保存限时调价活动 spu价格
	 * @param timeLimitedDiscountSpu 限时调价活动 spu价格
	 */
	void save(@Param("timeLimitedDiscountSpu") TimeLimitedDiscountSpu timeLimitedDiscountSpu);

	/**
	 * 更新限时调价活动 spu价格
	 * @param timeLimitedDiscountSpu 限时调价活动 spu价格
	 */
	void update(@Param("timeLimitedDiscountSpu") TimeLimitedDiscountSpu timeLimitedDiscountSpu);

	/**
	 * 根据限时调价活动 spu价格id删除限时调价活动 spu价格
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	void removeByActivity(@Param("activityId") Integer activityId);

    List<TimeLimitedDiscountSpuVO> selectByActivityId(@Param("activityId") Integer activityId);
}
