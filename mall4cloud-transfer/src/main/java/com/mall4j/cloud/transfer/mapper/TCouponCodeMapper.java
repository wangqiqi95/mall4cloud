package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.TCouponCode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券码关联表
 *
 * @author FrozenWatermelon
 * @date 2022-04-08 23:40:38
 */
public interface TCouponCodeMapper {

	/**
	 * 获取优惠券码关联表列表
	 * @return 优惠券码关联表列表
	 */
	List<TCouponCode> list();

	/**
	 * 根据优惠券码关联表id获取优惠券码关联表
	 *
	 * @param id 优惠券码关联表id
	 * @return 优惠券码关联表
	 */
	TCouponCode getById(@Param("id") Long id);

	/**
	 * 保存优惠券码关联表
	 * @param tCouponCode 优惠券码关联表
	 */
	void save(@Param("tCouponCode") TCouponCode tCouponCode);

	/**
	 * 更新优惠券码关联表
	 * @param tCouponCode 优惠券码关联表
	 */
	void update(@Param("tCouponCode") TCouponCode tCouponCode);

	/**
	 * 根据优惠券码关联表id删除优惠券码关联表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
