package com.mall4j.cloud.discount.mapper;

import com.mall4j.cloud.discount.model.DiscountSpu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 满减满折商品关联表
 *
 * @author FrozenWatermelon
 * @date 2020-12-10 13:43:38
 */
public interface DiscountSpuMapper {
	/**
	 * 根据满减满折id删除满减满折商品关联表
	 *
	 * @param discountId
	 */
	void deleteByDiscountId(@Param("discountId") Long discountId);

	/**
	 * 批量插入活动和商品关联信息
	 *
	 * @param discountSpus 活动和商品关联信息列表
	 */
	void insertDiscountSpuList(@Param("discountSpuList") List<DiscountSpu> discountSpus);

	/**
	 * 获取活动商品信息
	 *
	 * @param discountId 活动id
	 * @return 活动商品信息
	 */
	List<DiscountSpu> getDiscountSpuByDiscountId(@Param("discountId") Long discountId);

	/**
	 * 批量删除商品和活动的关联信息
	 *
	 * @param spuIds 商品ids
	 */
	void deleteBySpuIds(@Param("spuIds") List<Long> spuIds);

	/**
	 * 获取关联的商品列表
	 *
	 * @param discountId
	 * @return
	 */
    List<Long> listSpuIdByDiscountId(@Param("discountId") Long discountId);
}
