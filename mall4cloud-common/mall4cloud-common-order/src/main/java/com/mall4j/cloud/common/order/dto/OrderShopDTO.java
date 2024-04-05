package com.mall4j.cloud.common.order.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author FrozenWatermelon
 */
public class OrderShopDTO {

	/** 店铺ID **/
	@ApiModelProperty(value = "店铺id",required=true)
	private Long shopId;

	/**
	 * 订单备注信息
	 */
	@ApiModelProperty(value = "订单备注信息",required=true)
	private String remarks;

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "OrderShopDTO{" +
				"shopId=" + shopId +
				", remarks='" + remarks + '\'' +
				'}';
	}
}
