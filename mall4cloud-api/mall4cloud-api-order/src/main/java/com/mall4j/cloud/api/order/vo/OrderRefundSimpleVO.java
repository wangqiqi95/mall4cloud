package com.mall4j.cloud.api.order.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Author lth
 * @Date 2021/7/15 14:52
 */
public class OrderRefundSimpleVO {

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("退款单号")
    private Long refundId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getRefundId() {
        return refundId;
    }

    public void setRefundId(Long refundId) {
        this.refundId = refundId;
    }

    @Override
    public String toString() {
        return "OrderRefundSimpleVO{" +
                "orderId=" + orderId +
                ", refundId=" + refundId +
                '}';
    }
}
