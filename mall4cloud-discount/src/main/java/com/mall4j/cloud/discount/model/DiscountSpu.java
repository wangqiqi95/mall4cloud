package com.mall4j.cloud.discount.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 满减满折商品关联表
 *
 * @author FrozenWatermelon
 * @date 2020-12-10 13:43:38
 */
public class DiscountSpu extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 满减 商品 联合id
     */
    private Long discountSpuId;

    /**
     * 满减id
     */
    private Long discountId;

    /**
     * 商品id
     */
    private Long spuId;

	public Long getDiscountSpuId() {
		return discountSpuId;
	}

	public void setDiscountSpuId(Long discountSpuId) {
		this.discountSpuId = discountSpuId;
	}

	public Long getDiscountId() {
		return discountId;
	}

	public void setDiscountId(Long discountId) {
		this.discountId = discountId;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	@Override
	public String toString() {
		return "DiscountSpu{" +
				"discountSpuId=" + discountSpuId +
				",discountId=" + discountId +
				",spuId=" + spuId +
				'}';
	}
}
