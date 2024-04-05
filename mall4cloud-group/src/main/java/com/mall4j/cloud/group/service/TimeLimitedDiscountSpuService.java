package com.mall4j.cloud.group.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.group.model.TimeLimitedDiscountSpu;

/**
 * 限时调价活动 spu价格
 *
 * @author FrozenWatermelon
 * @date 2022-03-10 13:29:18
 */
public interface TimeLimitedDiscountSpuService {

	/**
	 * 分页获取限时调价活动 spu价格列表
	 * @param pageDTO 分页参数
	 * @return 限时调价活动 spu价格列表分页数据
	 */
	PageVO<TimeLimitedDiscountSpu> page(PageDTO pageDTO);

	/**
	 * 根据限时调价活动 spu价格id获取限时调价活动 spu价格
	 *
	 * @param id 限时调价活动 spu价格id
	 * @return 限时调价活动 spu价格
	 */
	TimeLimitedDiscountSpu getById(Long id);

	/**
	 * 保存限时调价活动 spu价格
	 * @param timeLimitedDiscountSpu 限时调价活动 spu价格
	 */
	void save(TimeLimitedDiscountSpu timeLimitedDiscountSpu);

	/**
	 * 更新限时调价活动 spu价格
	 * @param timeLimitedDiscountSpu 限时调价活动 spu价格
	 */
	void update(TimeLimitedDiscountSpu timeLimitedDiscountSpu);

	/**
	 * 根据限时调价活动 spu价格id删除限时调价活动 spu价格
	 * @param id 限时调价活动 spu价格id
	 */
	void deleteById(Long id);

	void removeByActivity(Integer activityId);
}
