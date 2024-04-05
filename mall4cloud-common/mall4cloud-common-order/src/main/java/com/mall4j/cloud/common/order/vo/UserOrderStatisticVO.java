package com.mall4j.cloud.common.order.vo;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户相关订单统计数据
 * @author: cl
 * @date: 2021-04-14 14:04:01
 */
public class UserOrderStatisticVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("最近消费时间")
    private Date reConsTime;

    @ApiModelProperty("消费金额")
    private BigDecimal consAmount;

    @ApiModelProperty("实付金额")
    private BigDecimal actualAmount;

    @ApiModelProperty("消费次数")
    private Integer consTimes;

    @ApiModelProperty("平均折扣")
    private BigDecimal averDiscount;

    @ApiModelProperty("优惠总金额")
    private BigDecimal reduceAmount;

    @ApiModelProperty("售后金额")
    private BigDecimal afterSaleAmount;

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

    public BigDecimal getConsAmount() {
        return consAmount;
    }

    public void setConsAmount(BigDecimal consAmount) {
        this.consAmount = consAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Integer getConsTimes() {
        return consTimes;
    }

    public void setConsTimes(Integer consTimes) {
        this.consTimes = consTimes;
    }

    public BigDecimal getAverDiscount() {
        return averDiscount;
    }

    public void setAverDiscount(BigDecimal averDiscount) {
        this.averDiscount = averDiscount;
    }

    public BigDecimal getReduceAmount() {
        return reduceAmount;
    }

    public void setReduceAmount(BigDecimal reduceAmount) {
        this.reduceAmount = reduceAmount;
    }

    public BigDecimal getAfterSaleAmount() {
        return afterSaleAmount;
    }

    public void setAfterSaleAmount(BigDecimal afterSaleAmount) {
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
                ", reduceAmount=" + reduceAmount +
                ", afterSaleAmount=" + afterSaleAmount +
                ", afterSaleTimes=" + afterSaleTimes +
                '}';
    }
}
