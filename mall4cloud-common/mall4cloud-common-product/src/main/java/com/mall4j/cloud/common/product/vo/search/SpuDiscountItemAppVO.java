package com.mall4j.cloud.common.product.vo.search;

import io.swagger.annotations.ApiModelProperty;

/**
 * 满减满折优惠项VO
 *
 * @author FrozenWatermelon
 * @date 2020-12-16 16:25:09
 */
public class SpuDiscountItemAppVO{

    @ApiModelProperty("满减满折优惠项id")
    private Long discountItemId;

    @ApiModelProperty("所需需要金额")
    private Long needAmount;

    @ApiModelProperty("优惠（元/折）9.5折就是95、9.5元就是950")
    private Long discount;

	public Long getDiscountItemId() {
		return discountItemId;
	}

	public void setDiscountItemId(Long discountItemId) {
		this.discountItemId = discountItemId;
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
		return "DiscountItemVO{" +
				"discountItemId=" + discountItemId +
				",needAmount=" + needAmount +
				",discount=" + discount +
				'}';
	}
}
