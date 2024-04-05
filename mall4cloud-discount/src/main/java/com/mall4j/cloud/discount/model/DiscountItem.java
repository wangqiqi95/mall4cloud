package com.mall4j.cloud.discount.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 满减满折优惠项
 *
 * @author FrozenWatermelon
 * @date 2020-12-10 13:43:39
 */
public class DiscountItem extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 满减满折优惠项id
     */
    private Long discountItemId;

    /**
     * 满减满折优惠id
     */
    private Long discountId;

    /**
     * 所需需要金额
     */
    private Long needAmount;

    /**
     * 优惠（元/折）
     */
    private Long discount;

	public Long getDiscountItemId() {
		return discountItemId;
	}

	public void setDiscountItemId(Long discountItemId) {
		this.discountItemId = discountItemId;
	}

	public Long getDiscountId() {
		return discountId;
	}

	public void setDiscountId(Long discountId) {
		this.discountId = discountId;
	}

	public Long getNeedAmount() {
		return needAmount;
	}

	public void setNeedAmount(Long needAmount) {
		this.needAmount = needAmount;
	}

	public Long getDiscount() {
		return discount;
	}

	public void setDiscount(Long discount) {
		this.discount = discount;
	}

	@Override
	public String toString() {
		return "DiscountItem{" +
				"discountItemId=" + discountItemId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",discountId=" + discountId +
				",needAmount=" + needAmount +
				",discount=" + discount +
				'}';
	}
}
