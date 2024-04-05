package com.mall4j.cloud.discount.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 满减满折优惠项VO
 *
 * @author FrozenWatermelon
 * @date 2020-12-16 16:25:09
 */
public class DiscountItemVO extends BaseVO{

    @ApiModelProperty("满减满折优惠项id")
    private Long discountItemId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("满减满折优惠id")
    private Long discountId;

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
				",discountId=" + discountId +
				",needAmount=" + needAmount +
				",discount=" + discount +
				'}';
	}
}
