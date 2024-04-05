package com.mall4j.cloud.user.mapper;

import com.mall4j.cloud.user.model.UserRechargeCoupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 余额优惠券关联表
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 15:51:36
 */
public interface UserRechargeCouponMapper {

	/**
	 * 获取余额优惠券关联表列表
	 *
	 * @return 余额优惠券关联表列表
	 */
	List<UserRechargeCoupon> list();

	/**
	 * 根据余额优惠券关联表id获取余额优惠券关联表
	 *
	 * @param rechargeId 余额优惠券关联表id
	 * @return 余额优惠券关联表
	 */
	UserRechargeCoupon getByRechargeId(@Param("rechargeId") Long rechargeId);

	/**
	 * 保存余额优惠券关联表
	 *
	 * @param userRechargeCoupon 余额优惠券关联表
	 */
	void save(@Param("userRechargeCoupon") UserRechargeCoupon userRechargeCoupon);

	/**
	 * 更新余额优惠券关联表
	 *
	 * @param userRechargeCoupon 余额优惠券关联表
	 */
	void update(@Param("userRechargeCoupon") UserRechargeCoupon userRechargeCoupon);

	/**
	 * 根据余额优惠券关联表id删除余额优惠券关联表
	 *
	 * @param rechargeId
	 */
	void deleteById(@Param("rechargeId") Long rechargeId);

	/**
	 * 批量保存充值赠送优惠券
	 *
	 * @param userRechargeCouponList
	 */
	void saveBatch(@Param("userRechargeCouponList") List<UserRechargeCoupon> userRechargeCouponList);

	/**
	 * 删除充值表详情中的一些关联优惠券
	 *
	 * @param rechargeId
	 * @param couponIdList
	 */
	void removeByRechargeIdAndCouponId(@Param("rechargeId") Long rechargeId, @Param("couponIdList") Set<Long> couponIdList);

	/**
	 * 修改充值表详情中的一些关联优惠券
	 *
	 * @param rechargeId
	 * @param couponList
	 */
	void updateBatchByCoupons(@Param("rechargeId") Long rechargeId, @Param("couponList") List<UserRechargeCoupon> couponList);

	/**
	 * 根据优惠券id列表，获取充值模板id列表
	 *
	 * @param couponIds
	 * @return 充值模板id列表
	 */
    List<Long> listRechargeIdByCouponIds(@Param("couponIds") List<Long> couponIds);

	/**
	 * 根据优惠券id列表删除
	 *
	 * @param couponIds 优惠券id列表
	 */
	void removeByCouponIds(@Param("couponIds") List<Long> couponIds);
}
