package com.mall4j.cloud.coupon.service;

import com.mall4j.cloud.api.coupon.dto.LockCouponDTO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.model.CouponLock;

import java.util.List;

/**
 * 优惠券使用记录
 *
 * @author FrozenWatermelon
 * @date 2020-12-28 10:04:50
 */
public interface CouponLockService {

	/**
	 * 分页获取优惠券使用记录列表
	 * @param pageDTO 分页参数
	 * @return 优惠券使用记录列表分页数据
	 */
	PageVO<CouponLock> page(PageDTO pageDTO);

	/**
	 * 根据优惠券使用记录id获取优惠券使用记录
	 *
	 * @param id 优惠券使用记录id
	 * @return 优惠券使用记录
	 */
	CouponLock getById(Long id);

	/**
	 * 保存优惠券使用记录
	 * @param couponLock 优惠券使用记录
	 */
	void save(CouponLock couponLock);

	/**
	 * 更新优惠券使用记录
	 * @param couponLock 优惠券使用记录
	 */
	void update(CouponLock couponLock);

	/**
	 * 根据优惠券使用记录id删除优惠券使用记录
	 * @param couponUseRecordId 优惠券使用记录id
	 */
	void deleteById(Long couponUseRecordId);


	/**
	 * 锁定订优惠券状态
	 * @param lockCouponParams 订单id和优惠券id关联信息
	 * @return void
	 */
	ServerResponseEntity<Void> lockCoupon(List<LockCouponDTO> lockCouponParams);

	/**
	 * 根据订单号进行优惠券解锁
	 * @param orderIds 订单ids
	 */
	void unlockCoupon(List<Long> orderIds);

	/**
	 * 正式锁定优惠券，标记为使用状态
	 * @param orderIds 订单ids
	 */
    void markerCouponUse(List<Long> orderIds);

	/**
	 * 还原订单使用的商家优惠券
	 * @param orderId
	 */
	void reductionCoupon(Long orderId);
}
