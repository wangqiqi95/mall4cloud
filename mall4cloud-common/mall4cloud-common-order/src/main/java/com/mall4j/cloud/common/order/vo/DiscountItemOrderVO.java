package com.mall4j.cloud.common.order.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 满减满折优惠项VO
 *
 * @author FrozenWatermelon
 * @date 2020-12-10 13:43:39
 */
public class DiscountItemOrderVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("满减满折优惠项id")
    private Long discountItemId;

    @ApiModelProperty("满减满折优惠id")
    private Long discountId;

    @ApiModelProperty("所需需要金额")
    private Long needAmount;

    @ApiModelProperty("优惠（元/折）")
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
		return "DiscountItemVO{" +
				"discountItemId=" + discountItemId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",discountId=" + discountId +
				",needAmount=" + needAmount +
				",discount=" + discount +
				'}';
	}
}
