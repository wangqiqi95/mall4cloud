package com.mall4j.cloud.api.order.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Author lth
 * @Date 2021/7/14 13:28
 */
public class OrderSettlementSimpleVO {

    @ApiModelProperty("订单id列表")
    private List<Long> orderIds;

    @ApiModelProperty("支付单号")
    private Long payId;

    public List<Long> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<Long> orderIds) {
        this.orderIds = orderIds;
    }

    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
    }

    @Override
    public String toString() {
        return "OrderSettlementSimpleVO{" +
                "orderIds=" + orderIds +
                ", payId=" + payId +
                '}';
    }
}
