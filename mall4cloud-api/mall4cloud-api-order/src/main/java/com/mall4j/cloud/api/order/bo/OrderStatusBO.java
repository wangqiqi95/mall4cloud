package com.mall4j.cloud.api.order.bo;

import java.util.Date;

/**
 * @author FrozenWatermelon
 * @date 2020/12/30
 */
public class OrderStatusBO {

    private Long orderId;

    private Integer status;

    private Integer orderType;

    private Long userId;

    private Integer isPayed;

    private Date createTime;
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Integer getIsPayed() {
        return isPayed;
    }

    public void setIsPayed(Integer isPayed) {
        this.isPayed = isPayed;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }
    
    @Override
    public String toString() {
        return "OrderStatusBO{" +
                "orderId=" + orderId +
                ", status=" + status +
                ", orderType=" + orderType +
                ", userId=" + userId +
                ", isPayed=" + isPayed +
                ", createTime=" + createTime +
                '}';
    }
}
