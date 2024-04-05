package com.mall4j.cloud.group.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.model.TimeLimitedDiscountShop;

import java.util.List;

/**
 * 限时调价活动 商铺
 *
 * @author FrozenWatermelon
 * @date 2022-03-10 01:55:14
 */
public interface TimeLimitedDiscountShopService {

	/**
	 * 分页获取限时调价活动 商铺列表
	 * @param pageDTO 分页参数
	 * @return 限时调价活动 商铺列表分页数据
	 */
	PageVO<TimeLimitedDiscountShop> page(PageDTO pageDTO);

	/**
	 * 根据限时调价活动 商铺id获取限时调价活动 商铺
	 *
	 * @param id 限时调价活动 商铺id
	 * @return 限时调价活动 商铺
	 */
	TimeLimitedDiscountShop getById(Long id);

	/**
	 * 保存限时调价活动 商铺
	 * @param timeLimitedDiscountShop 限时调价活动 商铺
	 */
	void save(TimeLimitedDiscountShop timeLimitedDiscountShop);

	/**
	 * 更新限时调价活动 商铺
	 * @param timeLimitedDiscountShop 限时调价活动 商铺
	 */
	void update(TimeLimitedDiscountShop timeLimitedDiscountShop);

	/**
	 * 根据限时调价活动 商铺id删除限时调价活动 商铺
	 * @param id 限时调价活动 商铺id
	 */
	void deleteById(Long id);

	void bathcInsert(Integer activityId,List<Long> shopIds);

	void removeByActivityId(Integer activityId);


}
