package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.TCoupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券设置
 *
 * @author FrozenWatermelon
 * @date 2022-04-08 23:40:38
 */
public interface TCouponMapper {

	/**
	 * 获取优惠券设置列表
	 * @return 优惠券设置列表
	 */
	List<TCoupon> list();

	/**
	 * 根据优惠券设置id获取优惠券设置
	 *
	 * @param id 优惠券设置id
	 * @return 优惠券设置
	 */
	TCoupon getById(@Param("id") Long id);

	/**
	 * 保存优惠券设置
	 * @param tCoupon 优惠券设置
	 */
	void save(@Param("tCoupon") TCoupon tCoupon);

	/**
	 * 更新优惠券设置
	 * @param tCoupon 优惠券设置
	 */
	void update(@Param("tCoupon") TCoupon tCoupon);

	/**
	 * 根据优惠券设置id删除优惠券设置
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
