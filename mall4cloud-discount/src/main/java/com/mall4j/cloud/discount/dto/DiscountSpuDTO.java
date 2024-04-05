package com.mall4j.cloud.discount.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 满减满折商品关联表DTO
 *
 * @author FrozenWatermelon
 * @date 2020-12-10 13:43:38
 */
public class DiscountSpuDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("满减 商品 联合id")
    private Long discountSpuId;

    @ApiModelProperty("满减id")
    private Long discountId;

    @ApiModelProperty("商品id")
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
		return "DiscountSpuDTO{" +
				"discountSpuId=" + discountSpuId +
				",discountId=" + discountId +
				",spuId=" + spuId +
				'}';
	}
}
