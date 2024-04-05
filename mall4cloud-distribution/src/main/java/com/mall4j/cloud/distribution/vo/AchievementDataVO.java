package com.mall4j.cloud.distribution.vo;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 分销员业绩数据
 * @author cl
 * @date 2021-08-16 14:53:11
 */
public class AchievementDataVO {

    @ApiModelProperty(value = "积累绑定客户数")
    private Integer boundCustomers;

    @ApiModelProperty(value = "积累邀请分销员数")
    private Integer invitedDistributors;

    @ApiModelProperty(value = "积累推广订单数")
    private Integer orderCount;

    @ApiModelProperty("待结算金额")
    private BigDecimal unsettledAmount;

    @ApiModelProperty("可提现金额")
    private BigDecimal settledAmount;

    @ApiModelProperty("已失效金额")
    private BigDecimal invalidAmount;

    @ApiModelProperty("积累收益")
    private BigDecimal accumulateAmount;

    public Integer getBoundCustomers() {
        return boundCustomers;
    }

    public void setBoundCustomers(Integer boundCustomers) {
        this.boundCustomers = boundCustomers;
    }

    public Integer getInvitedDistributors() {
        return invitedDistributors;
    }

    public void setInvitedDistributors(Integer invitedDistributors) {
        this.invitedDistributors = invitedDistributors;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public BigDecimal getUnsettledAmount() {
        return unsettledAmount;
    }

    public void setUnsettledAmount(BigDecimal unsettledAmount) {
        this.unsettledAmount = unsettledAmount;
    }

    public BigDecimal getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(BigDecimal settledAmount) {
        this.settledAmount = settledAmount;
    }

    public BigDecimal getInvalidAmount() {
        return invalidAmount;
    }

    public void setInvalidAmount(BigDecimal invalidAmount) {
        this.invalidAmount = invalidAmount;
    }

    public BigDecimal getAccumulateAmount() {
        return accumulateAmount;
    }

    public void setAccumulateAmount(BigDecimal accumulateAmount) {
        this.accumulateAmount = accumulateAmount;
    }

    @Override
    public String toString() {
        return "AchievementDataVO{" +
                "boundCustomers=" + boundCustomers +
                ", invitedDistributors=" + invitedDistributors +
                ", orderCount=" + orderCount +
                ", unsettledAmount=" + unsettledAmount +
                ", settledAmount=" + settledAmount +
                ", invalidAmount=" + invalidAmount +
                ", accumulateAmount=" + accumulateAmount +
                '}';
    }
}
