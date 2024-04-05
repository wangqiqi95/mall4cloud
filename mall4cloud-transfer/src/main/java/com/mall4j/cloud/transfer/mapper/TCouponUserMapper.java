package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.TCouponUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券用户关联信息
 *
 * @author FrozenWatermelon
 * @date 2022-04-08 23:40:39
 */
public interface TCouponUserMapper {

	/**
	 * 获取优惠券用户关联信息列表
	 * @return 优惠券用户关联信息列表
	 */
	List<TCouponUser> list();

	/**
	 * 根据优惠券用户关联信息id获取优惠券用户关联信息
	 *
	 * @param id 优惠券用户关联信息id
	 * @return 优惠券用户关联信息
	 */
	TCouponUser getById(@Param("id") Long id);

	/**
	 * 保存优惠券用户关联信息
	 * @param tCouponUser 优惠券用户关联信息
	 */
	void save(@Param("tCouponUser") TCouponUser tCouponUser);

	void batchSave(@Param("tCouponUsers") List<TCouponUser> tCouponUsers);

	/**
	 * 更新优惠券用户关联信息
	 * @param tCouponUser 优惠券用户关联信息
	 */
	void update(@Param("tCouponUser") TCouponUser tCouponUser);

	/**
	 * 根据优惠券用户关联信息id删除优惠券用户关联信息
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
