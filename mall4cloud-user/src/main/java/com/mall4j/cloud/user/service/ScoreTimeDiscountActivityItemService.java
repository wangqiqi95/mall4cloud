package com.mall4j.cloud.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.model.ScoreTimeDiscountActivityItem;

/**
 * 积分限时折扣兑换券
 *
 * @author gmq
 * @date 2022-07-11 15:12:37
 */
public interface ScoreTimeDiscountActivityItemService extends IService<ScoreTimeDiscountActivityItem> {

	/**
	 * 分页获取积分限时折扣兑换券列表
	 * @param pageDTO 分页参数
	 * @return 积分限时折扣兑换券列表分页数据
	 */
	PageVO<ScoreTimeDiscountActivityItem> page(PageDTO pageDTO);

	/**
	 * 根据积分限时折扣兑换券id获取积分限时折扣兑换券
	 *
	 * @param id 积分限时折扣兑换券id
	 * @return 积分限时折扣兑换券
	 */
	ScoreTimeDiscountActivityItem getById(Long id);

	/**
	 * 保存积分限时折扣兑换券
	 * @param scoreTimeDiscountActivityItem 积分限时折扣兑换券
	 */
	void saveTo(ScoreTimeDiscountActivityItem scoreTimeDiscountActivityItem);

	/**
	 * 更新积分限时折扣兑换券
	 * @param scoreTimeDiscountActivityItem 积分限时折扣兑换券
	 */
	void updateTo(ScoreTimeDiscountActivityItem scoreTimeDiscountActivityItem);

	/**
	 * 根据积分限时折扣兑换券id删除积分限时折扣兑换券
	 * @param id 积分限时折扣兑换券id
	 */
	void deleteById(Long id);
}
