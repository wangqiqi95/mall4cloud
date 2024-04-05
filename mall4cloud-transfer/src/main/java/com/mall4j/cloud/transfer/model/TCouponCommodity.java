package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 优惠券商品关联表
 *
 * @author FrozenWatermelon
 * @date 2022-04-08 23:40:38
 */
public class TCouponCommodity extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 商品id
     */
    private Long commodityId;

    /**
     * 商品code,可能会存在优惠券比商品先同步过来的情况，导致商品id时空
     */
    private String spuCode;

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

	public Long getCommodityId() {
		return commodityId;
	}

	public void setCommodityId(Long commodityId) {
		this.commodityId = commodityId;
	}

	public String getSpuCode() {
		return spuCode;
	}

	public void setSpuCode(String spuCode) {
		this.spuCode = spuCode;
	}

	@Override
	public String toString() {
		return "TCouponCommodity{" +
				"id=" + id +
				",couponId=" + couponId +
				",commodityId=" + commodityId +
				",spuCode=" + spuCode +
				'}';
	}
}
