package com.mall4j.cloud.api.user.bo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 用户相关订单统计数据
 * @author: cl
 * @date: 2021-04-14 14:04:01
 */
public class UserOrderStatisticBO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("最近消费时间")
    private Date reConsTime;

    @ApiModelProperty("消费金额")
    private Double consAmount;

    @ApiModelProperty("实付金额")
    private Long actualAmount;

    @ApiModelProperty("消费次数")
    private Integer consTimes;

    @ApiModelProperty("平均折扣")
    private Double averDiscount;

    @ApiModelProperty("售后金额")
    private Double afterSaleAmount;

    @ApiModelProperty("售后次数")
    private Integer afterSaleTimes;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getReConsTime() {
        return reConsTime;
    }

    public void setReConsTime(Date reConsTime) {
        this.reConsTime = reConsTime;
    }

    public Double getConsAmount() {
        return consAmount;
    }

    public void setConsAmount(Double consAmount) {
        this.consAmount = consAmount;
    }

    public Long getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Long actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Integer getConsTimes() {
        return consTimes;
    }

    public void setConsTimes(Integer consTimes) {
        this.consTimes = consTimes;
    }

    public Double getAverDiscount() {
        return averDiscount;
    }

    public void setAverDiscount(Double averDiscount) {
        this.averDiscount = averDiscount;
    }

    public Double getAfterSaleAmount() {
        return afterSaleAmount;
    }

    public void setAfterSaleAmount(Double afterSaleAmount) {
        this.afterSaleAmount = afterSaleAmount;
    }

    public Integer getAfterSaleTimes() {
        return afterSaleTimes;
    }

    public void setAfterSaleTimes(Integer afterSaleTimes) {
        this.afterSaleTimes = afterSaleTimes;
    }

    @Override
    public String toString() {
        return "UserOrderStatisticVO{" +
                "userId=" + userId +
                ", reConsTime=" + reConsTime +
                ", consAmount=" + consAmount +
                ", actualAmount=" + actualAmount +
                ", consTimes=" + consTimes +
                ", averDiscount=" + averDiscount +
                ", afterSaleAmount=" + afterSaleAmount +
                ", afterSaleTimes=" + afterSaleTimes +
                '}';
    }
}
