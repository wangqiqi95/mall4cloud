package com.mall4j.cloud.group.mapper;

import com.mall4j.cloud.group.model.TimeLimitedDiscountShop;
import com.mall4j.cloud.group.vo.TimeLimitedDiscountShopVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 限时调价活动 商铺
 *
 * @author FrozenWatermelon
 * @date 2022-03-10 01:55:14
 */
public interface TimeLimitedDiscountShopMapper {

	/**
	 * 获取限时调价活动 商铺列表
	 * @return 限时调价活动 商铺列表
	 */
	List<TimeLimitedDiscountShop> list();

	/**
	 * 根据限时调价活动 商铺id获取限时调价活动 商铺
	 *
	 * @param id 限时调价活动 商铺id
	 * @return 限时调价活动 商铺
	 */
	TimeLimitedDiscountShop getById(@Param("id") Long id);

	/**
	 * 保存限时调价活动 商铺
	 * @param timeLimitedDiscountShop 限时调价活动 商铺
	 */
	void save(@Param("timeLimitedDiscountShop") TimeLimitedDiscountShop timeLimitedDiscountShop);

	/**
	 * 更新限时调价活动 商铺
	 * @param timeLimitedDiscountShop 限时调价活动 商铺
	 */
	void update(@Param("timeLimitedDiscountShop") TimeLimitedDiscountShop timeLimitedDiscountShop);

	/**
	 * 根据限时调价活动 商铺id删除限时调价活动 商铺
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	void deleteByActivityIdAndShopId(@Param("activityId")Integer activityId,@Param("shopId")Integer shopId);

	void insertBatch(@Param("list") List<TimeLimitedDiscountShop> list);

    void removeByActivityId(@Param("activityId") Integer activityId);

    List<TimeLimitedDiscountShopVO> selectByActivityId(@Param("activityId") Integer activityId);

    TimeLimitedDiscountShopVO selectByActivityIdAndShopId(@Param("activityId") Integer activityId,
                                                          @Param("shopId") Integer shopId);
}
