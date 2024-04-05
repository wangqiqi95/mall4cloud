package com.mall4j.cloud.api.user.bo;

import java.util.List;
/**
 * @author FrozenWatermelon
 * @date 2020/12/22
 */
public class UserScoreBO {

    private Long userId;

    private Integer payType;

    private List<Long> orderIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public List<Long> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<Long> orderIds) {
        this.orderIds = orderIds;
    }

    @Override
    public String toString() {
        return "UserScoreBo{" +
                "userId=" + userId +
                "payType=" + payType +
                ", orderIds=" + orderIds +
                '}';
    }
}
