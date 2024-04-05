package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.TCouponCommodity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券商品关联表
 *
 * @author FrozenWatermelon
 * @date 2022-04-08 23:40:38
 */
public interface TCouponCommodityMapper {

	/**
	 * 获取优惠券商品关联表列表
	 * @return 优惠券商品关联表列表
	 */
	List<TCouponCommodity> list();

	/**
	 * 根据优惠券商品关联表id获取优惠券商品关联表
	 *
	 * @param id 优惠券商品关联表id
	 * @return 优惠券商品关联表
	 */
	TCouponCommodity getById(@Param("id") Long id);

	/**
	 * 保存优惠券商品关联表
	 * @param tCouponCommodity 优惠券商品关联表
	 */
	void save(@Param("tCouponCommodity") TCouponCommodity tCouponCommodity);

	/**
	 * 更新优惠券商品关联表
	 * @param tCouponCommodity 优惠券商品关联表
	 */
	void update(@Param("tCouponCommodity") TCouponCommodity tCouponCommodity);

	/**
	 * 根据优惠券商品关联表id删除优惠券商品关联表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
