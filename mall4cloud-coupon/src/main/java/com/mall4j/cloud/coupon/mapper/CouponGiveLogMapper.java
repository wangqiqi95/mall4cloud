package com.mall4j.cloud.coupon.mapper;

import com.mall4j.cloud.coupon.model.CouponGiveLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券赠送记录
 *
 * @author FrozenWatermelon
 * @date 2021-04-28 16:25:05
 */
public interface CouponGiveLogMapper {

	/**
	 * 获取优惠券赠送记录列表
	 *
	 * @return 优惠券赠送记录列表
	 */
	List<CouponGiveLog> list();

	/**
	 * 根据优惠券赠送记录id获取优惠券赠送记录
	 *
	 * @param bizType 优惠券赠送记录id
	 * @return 优惠券赠送记录
	 */
	CouponGiveLog getByBizType(@Param("bizType") Long bizType);

	/**
	 * 保存优惠券赠送记录
	 *
	 * @param couponGiveLog 优惠券赠送记录
	 */
	void save(@Param("couponGiveLog") CouponGiveLog couponGiveLog);

	/**
	 * 更新优惠券赠送记录
	 *
	 * @param couponGiveLog 优惠券赠送记录
	 */
	void update(@Param("couponGiveLog") CouponGiveLog couponGiveLog);

	/**
	 * 根据优惠券赠送记录id删除优惠券赠送记录
	 *
	 * @param bizType
	 */
	void deleteById(@Param("bizType") Long bizType);

	/**
	 * 计算有没有已经保存的记录
	 * @param bizType 业务类型
	 * @param bizId 业务id
	 * @return 有没有已经保存的记录
	 */
    Integer count(@Param("bizType") Integer bizType, @Param("bizId") Long bizId);
}
