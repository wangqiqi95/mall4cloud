package com.mall4j.cloud.api.openapi.skq_erp.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 物流订单项信息DTO
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:00
 */
public class DeliveryOrderItemDTO{

    @ApiModelProperty("订单物流包裹id")
    private Long deliveryOrderId;

    @ApiModelProperty("商品图片")
    private String pic;

    @ApiModelProperty("商品名称")
    private String spuName;

    @ApiModelProperty("发货改变的数量")
    private Integer changeNum;

	public Long getDeliveryOrderId() {
		return deliveryOrderId;
	}

	public void setDeliveryOrderId(Long deliveryOrderId) {
		this.deliveryOrderId = deliveryOrderId;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getSpuName() {
		return spuName;
	}

	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}

	public Integer getChangeNum() {
		return changeNum;
	}

	public void setChangeNum(Integer changeNum) {
		this.changeNum = changeNum;
	}

	@Override
	public String toString() {
		return "DeliveryOrderItemDTO{" +
				", deliveryOrderId=" + deliveryOrderId +
				", pic='" + pic + '\'' +
				", spuName='" + spuName + '\'' +
				", changeNum=" + changeNum +
				'}';
	}
}
