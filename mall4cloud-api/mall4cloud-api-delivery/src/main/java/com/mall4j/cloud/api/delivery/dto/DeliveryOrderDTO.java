package com.mall4j.cloud.api.delivery.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 订单快递信息DTO
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:00
 */
public class DeliveryOrderDTO{

	@ApiModelProperty(value = "deliveryOrderId")
	private Long deliveryOrderId;

	@NotNull(message="订单号不能为空")
	@ApiModelProperty(value = "订单号",required=true)
	private Long orderId;

	@ApiModelProperty(value = "快递公司",required=true)
	private Long deliveryCompanyId;

	@ApiModelProperty(value = "快递公司code",required=true)
	private String deliveryCompanyCode;

	@ApiModelProperty(value = "物流单号",required=true)
	private String deliveryNo;

	@NotNull(message="发货方式不能为空")
	@ApiModelProperty(value = "发货方式",required=true)
	private Integer deliveryType;

	private List<DeliveryOrderItemDTO> selectOrderItems;


	public String getDeliveryCompanyCode() {
		return deliveryCompanyCode;
	}

	public void setDeliveryCompanyCode(String deliveryCompanyCode) {
		this.deliveryCompanyCode = deliveryCompanyCode;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getDeliveryCompanyId() {
		return deliveryCompanyId;
	}

	public void setDeliveryCompanyId(Long deliveryCompanyId) {
		this.deliveryCompanyId = deliveryCompanyId;
	}

	public String getDeliveryNo() {
		return deliveryNo;
	}

	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}

	public Integer getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}

	public List<DeliveryOrderItemDTO> getSelectOrderItems() {
		return selectOrderItems;
	}

	public void setSelectOrderItems(List<DeliveryOrderItemDTO> selectOrderItems) {
		this.selectOrderItems = selectOrderItems;
	}

	public Long getDeliveryOrderId() {
		return deliveryOrderId;
	}

	public void setDeliveryOrderId(Long deliveryOrderId) {
		this.deliveryOrderId = deliveryOrderId;
	}

	@Override
	public String toString() {
		return "DeliveryOrderDTO{" +
				"deliveryOrderId='" + deliveryOrderId + '\'' +
				"orderNumber='" + orderId + '\'' +
				", dvyId=" + deliveryCompanyId +
				", dvyFlowId='" + deliveryNo + '\'' +
				", deliveryType=" + deliveryType +
				", selectOrderItems=" + selectOrderItems +
				'}';
	}
}
