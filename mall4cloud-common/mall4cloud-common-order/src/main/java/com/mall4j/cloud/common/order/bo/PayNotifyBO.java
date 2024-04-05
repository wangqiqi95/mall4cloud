package com.mall4j.cloud.common.order.bo;

import java.util.List;

/**
 * 订单支付成功通知
 * @author FrozenWatermelon
 * @date 2020/12/8
 */
public class PayNotifyBO {

    private List<Long> orderIds;

    private Integer payType;

    private Long payId;

    public PayNotifyBO() {
    }

    public PayNotifyBO(List<Long> orderIds, Integer payType, Long payId) {
        this.orderIds = orderIds;
        this.payType = payType;
        this.payId = payId;
    }

    public List<Long> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<Long> orderIds) {
        this.orderIds = orderIds;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
    }

    @Override
    public String toString() {
        return "PayNotifyBO{" +
                "orderIds=" + orderIds +
                ", payType=" + payType +
                ", payId=" + payId +
                '}';
    }
}
