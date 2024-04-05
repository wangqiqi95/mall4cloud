package com.mall4j.cloud.user.mapper;

import com.mall4j.cloud.user.model.UserRightsCoupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2021-04-25 16:01:58
 */
public interface UserRightsCouponMapper {

	/**
	 * 获取列表
	 *
	 * @return 列表
	 */
	List<UserRightsCoupon> list();

	/**
	 * 根据id获取
	 *
	 * @param rightsCouponId id
	 * @return
	 */
	UserRightsCoupon getByRightsCouponId(@Param("rightsCouponId") Long rightsCouponId);

	/**
	 * 保存
	 *
	 * @param userRightsCoupon
	 */
	void save(@Param("userRightsCoupon") UserRightsCoupon userRightsCoupon);

	/**
	 * 更新
	 *
	 * @param userRightsCoupon
	 */
	void update(@Param("userRightsCoupon") UserRightsCoupon userRightsCoupon);

	/**
	 * 根据id删除
	 *
	 * @param rightsCouponId
	 */
	void deleteById(@Param("rightsCouponId") Long rightsCouponId);

	/**
	 * 根据权益id获取优惠券id列表
	 *
	 * @param rightsId 权益id
	 * @return 优惠券id列表
	 */
	List<Long> getCouponListByRightsId(@Param("rightsId") Long rightsId);

	/**
	 * 根据权益id删除
	 *
	 * @param rightsId 权益id
	 */
	void deleteByRightsId(@Param("rightsId") Long rightsId);

	/**
	 * 根据优惠券id列表，获取权益id列表
	 *
	 * @param couponIds 优惠券id列表
	 * @return 权益id列表
	 */
    List<Long> listRightsIdByCouponIds(@Param("couponIds") List<Long> couponIds);

	/**
	 * 根据优惠券id列表删除
	 *
	 * @param couponIds 优惠券id列表
	 */
	void deleteByCouponIds(@Param("couponIds") List<Long> couponIds);
}
