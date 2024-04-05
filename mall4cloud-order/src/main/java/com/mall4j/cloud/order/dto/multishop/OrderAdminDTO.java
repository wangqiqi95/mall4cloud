package com.mall4j.cloud.order.dto.multishop;

import com.mall4j.cloud.order.dto.OrderAddrDTO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 订单参数
 *
 * @author FrozenWatermelon
 * @date 2020-12-04 11:27:35
 */
public class OrderAdminDTO {

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "运费金额")
    private Long freightAmount;

    @ApiModelProperty(value = "订单项")
    private List<OrderItemDTO> orderItems;

    @ApiModelProperty(value = "用户收货地址")
    private OrderAddrDTO orderAddrDTO;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(Long freightAmount) {
        this.freightAmount = freightAmount;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public OrderAddrDTO getOrderAddrDTO() {
        return orderAddrDTO;
    }

    public void setOrderAddrDTO(OrderAddrDTO orderAddrDTO) {
        this.orderAddrDTO = orderAddrDTO;
    }

    @Override
    public String toString() {
        return "OrderAdminDTO{" +
                "orderId=" + orderId +
                ", shopId=" + shopId +
                ", freightAmount=" + freightAmount +
                ", orderItems=" + orderItems +
                ", orderAddrDTO=" + orderAddrDTO +
                '}';
    }
}
