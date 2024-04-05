package com.mall4j.cloud.api.order.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 订单信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
public class FlowOrderVO extends BaseVO {

	@ApiModelProperty(value = "订单ID")
    private Long orderId;

	@ApiModelProperty(value = "用户ID")
    private Long userId;

	@ApiModelProperty(value = "商品Id", required = true)
	private Long spuId;

	@ApiModelProperty(value = "订单金额", required = true)
	private Long orderAmount;

	@ApiModelProperty(value = "是否支付", required = true)
	private Integer isPayed;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Long getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Long orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Integer getIsPayed() {
		return isPayed;
	}

	public void setIsPayed(Integer isPayed) {
		this.isPayed = isPayed;
	}

	@Override
	public String toString() {
		return "FlowOrderVO{" +
				"orderId=" + orderId +
				"userId=" + userId +
				", spuId=" + spuId +
				", orderAmount=" + orderAmount +
				", isPayed=" + isPayed +
				'}';
	}
}
