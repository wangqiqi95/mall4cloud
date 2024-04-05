package com.mall4j.cloud.coupon.mapper;

import com.mall4j.cloud.coupon.model.CouponSpu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券商品关联信息
 *
 * @author YXF
 * @date 2020-12-08 17:22:57
 */
public interface CouponSpuMapper {
	/**
	 * 批量保存优惠券商品关联信息
	 *
	 * @param couponSpus 优惠券商品关联信息列表
	 */
	void saveBatch(@Param("couponSpus") List<CouponSpu> couponSpus);

	/**
	 * 根据优惠券id及spuId列表删除优惠券商品关联信息
	 *
	 * @param couponId
	 * @param spuIds
	 */
    void deleteByCouponIdAndSpuIds(@Param("couponId") Long couponId, @Param("spuIds") List<Long> spuIds);

	/**
	 * 根据商品id列表，删除优惠券关联商品信息
	 *
	 * @param spuIds
	 */
	void deleteBySpuIds(@Param("spuIds") List<Long> spuIds);
}
