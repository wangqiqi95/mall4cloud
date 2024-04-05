package com.mall4j.cloud.coupon.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.coupon.model.CouponGiveLog;

/**
 * 优惠券赠送记录
 *
 * @author FrozenWatermelon
 * @date 2021-04-28 16:25:05
 */
public interface CouponGiveLogService {

	/**
	 * 分页获取优惠券赠送记录列表
	 * @param pageDTO 分页参数
	 * @return 优惠券赠送记录列表分页数据
	 */
	PageVO<CouponGiveLog> page(PageDTO pageDTO);

	/**
	 * 根据优惠券赠送记录id获取优惠券赠送记录
	 *
	 * @param bizType 优惠券赠送记录id
	 * @return 优惠券赠送记录
	 */
	CouponGiveLog getByBizType(Long bizType);

	/**
	 * 保存优惠券赠送记录
	 * @param couponGiveLog 优惠券赠送记录
	 */
	void save(CouponGiveLog couponGiveLog);

	/**
	 * 更新优惠券赠送记录
	 * @param couponGiveLog 优惠券赠送记录
	 */
	void update(CouponGiveLog couponGiveLog);

	/**
	 * 根据优惠券赠送记录id删除优惠券赠送记录
	 * @param bizType 优惠券赠送记录id
	 */
	void deleteById(Long bizType);
}
