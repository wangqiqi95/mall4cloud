package com.mall4j.cloud.coupon.service;

import com.mall4j.cloud.coupon.dto.CouponDTO;
import com.mall4j.cloud.coupon.vo.CouponVO;

import java.util.List;

/**
 * 优惠券商品关联信息
 *
 * @author YXF
 * @date 2020-12-08 17:22:57
 */
public interface CouponSpuService {
	/**
	 * 保存优惠券商品关联信息
	 * @param couponId 优惠券id
	 * @param spuIds 商品id列表
	 */
	void save(Long couponId, List<Long> spuIds);

	/**
	 * 更新优惠券商品关联信息
	 * @param couponDTO 更新的优惠券信息
	 * @param couponDb 旧的优惠券信息
	 */
	void update(CouponDTO couponDTO, CouponVO couponDb);

	/**
	 * 根据商品id列表，删除优惠券关联商品信息
	 * @param spuIds
	 */
    void deleteBySpuIds(List<Long> spuIds);
}
