package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-04-08 23:40:39
 */
public class TCouponShop implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private Long couponId;

    /**
     * 
     */
    private Long shopId;

    /**
     * 
     */
    private String shopCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	@Override
	public String toString() {
		return "TCouponShop{" +
				"id=" + id +
				",couponId=" + couponId +
				",shopId=" + shopId +
				",shopCode=" + shopCode +
				'}';
	}
}
