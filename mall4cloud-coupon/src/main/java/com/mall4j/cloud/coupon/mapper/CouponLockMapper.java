package com.mall4j.cloud.coupon.mapper;

import com.mall4j.cloud.coupon.model.CouponLock;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券使用记录
 *
 * @author FrozenWatermelon
 * @date 2020-12-28 10:04:50
 */
public interface CouponLockMapper {

	/**
	 * 获取优惠券使用记录列表
	 *
	 * @return 优惠券使用记录列表
	 */
	List<CouponLock> list();

	/**
	 * 根据优惠券使用记录id获取优惠券使用记录
	 *
	 * @param id 优惠券使用记录id
	 * @return 优惠券使用记录
	 */
	CouponLock getById(@Param("id") Long id);

	/**
	 * 保存优惠券使用记录
	 *
	 * @param couponLock 优惠券使用记录
	 */
	void save(@Param("couponLock") CouponLock couponLock);

	/**
	 * 更新优惠券使用记录
	 *
	 * @param couponLock 优惠券使用记录
	 */
	void update(@Param("couponLock") CouponLock couponLock);

	/**
	 * 根据优惠券使用记录id删除优惠券使用记录
	 *
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 批量保存使用记录
	 *
	 * @param couponLocks
	 */
	void saveBatch(@Param("couponLocks") List<CouponLock> couponLocks);

	/**
	 * 获取需要解锁的、用户所领取的优惠券
	 *
	 * @param orderId 订单id
	 * @return 用户所领取的优惠券id
	 */
    List<Long> listCouponUserIdsByOrderId(Long orderId);

	/**
	 * 将锁定状态标记为已解锁
	 *
     * @param couponUserIds 用户所领取的优惠券id
     * @return
     */
	int unLockByIds(@Param("couponUserIds") List<Long> couponUserIds);

	/**
	 * 正式锁定优惠券，标记为使用状态
	 *
	 * @param orderIds 订单ids
	 */
    void markerCouponUse(@Param("orderIds") List<Long> orderIds);

	/**
	 * 获取用户在该订单使用的优惠
	 *
	 * @param orderId
	 * @return
	 */
	Long getUserCouponIdByOrderId(@Param("orderId") String orderId);

	/**
	 * 通过用户优惠券id，获取待确认、已锁定的 优惠券使用记录
	 * @param couponUserIds 用户优惠券id集合
	 * @return 优惠券使用记录集合
	 */
	List<CouponLock> getUnConfirmAndLockCoupon(@Param("couponUserIds") List<Long> couponUserIds);
}
