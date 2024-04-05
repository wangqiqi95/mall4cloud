package com.mall4j.cloud.payment.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 订单支付记录DTO
 *
 * @author FrozenWatermelon
 * @date 2020-12-25 09:50:59
 */
public class StaffPayInfoDTO extends BasePayInfoDTO{

	@NotEmpty(message = "订单号不能为空")
	@ApiModelProperty(value = "订单号", required = true)
	private List<Long> orderIds;

	@NotNull(message = "会员ID不能为空")
	@ApiModelProperty(value = "会员ID", required = true)
	private Long userId;

	public List<Long> getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(List<Long> orderIds) {
		this.orderIds = orderIds;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "StaffPayInfoDTO{" +
				"orderIds=" + orderIds +
				", userId=" + userId +
				'}';
	}
}
