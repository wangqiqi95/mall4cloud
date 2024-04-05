package com.mall4j.cloud.order.vo;

import com.mall4j.cloud.api.order.vo.OrderAddrVO;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 订单项和地址
 *
 * @author FrozenWatermelon
 * @date 2021-02-22 14:13:50
 */
public class OrderItemWithAddressVO {

	@ApiModelProperty(value = "订单项")
	private List<OrderItemVO> orderItems;

	@ApiModelProperty(value = "订单地址")
	private OrderAddrVO orderAddr;

	public List<OrderItemVO> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItemVO> orderItems) {
		this.orderItems = orderItems;
	}

	public OrderAddrVO getOrderAddr() {
		return orderAddr;
	}

	public void setOrderAddr(OrderAddrVO orderAddr) {
		this.orderAddr = orderAddr;
	}

	@Override
	public String toString() {
		return "OrderItemWithAddressVO{" +
				"orderItems=" + orderItems +
				", orderAddr=" + orderAddr +
				'}';
	}
}
