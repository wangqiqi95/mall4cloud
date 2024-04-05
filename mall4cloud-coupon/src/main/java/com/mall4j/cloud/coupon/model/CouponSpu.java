package com.mall4j.cloud.coupon.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 优惠券商品关联信息
 *
 * @author YXF
 * @date 2020-12-08 17:22:57
 */
public class CouponSpu extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 优惠券商品ID
     */
    private Long couponSpuId;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 商品ID
     */
    private Long spuId;

	public Long getCouponSpuId() {
		return couponSpuId;
	}

	public void setCouponSpuId(Long couponSpuId) {
		this.couponSpuId = couponSpuId;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	@Override
	public String toString() {
		return "CouponProd{" +
				"couponSpuId=" + couponSpuId +
				",couponId=" + couponId +
				",spuId=" + spuId +
				'}';
	}
}
