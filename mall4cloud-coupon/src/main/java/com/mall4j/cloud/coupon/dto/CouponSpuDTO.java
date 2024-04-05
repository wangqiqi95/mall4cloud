package com.mall4j.cloud.coupon.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 优惠券商品关联信息DTO
 *
 * @author YXF
 * @date 2020-12-08 17:22:57
 */
public class CouponSpuDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("优惠券商品ID")
    private Long couponProdId;

    @ApiModelProperty("优惠券ID")
    private Long couponId;

    @ApiModelProperty("商品ID")
    private Long spuId;

	public Long getCouponProdId() {
		return couponProdId;
	}

	public void setCouponProdId(Long couponProdId) {
		this.couponProdId = couponProdId;
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
		return "CouponProdDTO{" +
				"couponProdId=" + couponProdId +
				",couponId=" + couponId +
				",spuId=" + spuId +
				'}';
	}
}
