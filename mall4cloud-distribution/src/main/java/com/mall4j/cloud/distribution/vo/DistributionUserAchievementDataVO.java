package com.mall4j.cloud.distribution.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author cl
 * @date 2021-08-10 14:04:24
 */
public class DistributionUserAchievementDataVO {

    @ApiModelProperty(value = "积累绑定客户数")
    private Integer boundCustomers;

    @ApiModelProperty(value = "积累邀请分销员数")
    private Integer invitedVeeker;

    @ApiModelProperty(value = "积累支付单数")
    private Integer payNumber;

    @ApiModelProperty(value = "积累成功成交单数")
    private Integer successOrderNumber;

    @ApiModelProperty(value = "积累成功成交金额")
    private Integer successTradingVolume;

    @ApiModelProperty(value = "积累推广订单数")
    private Integer orderNumber;

    @ApiModelProperty(value = "用户消费笔数")
    private Double expenseNumber;

    @ApiModelProperty(value = "用户消费金额")
    private Double sumOfConsumption;

    @ApiModelProperty(value = "分销员收益明细")
    private DistributionUserIncomeVO distributionUserIncomeVO;

    @ApiModelProperty(value = "分销员钱包")
    private DistributionUserWalletVO distributionUserWalletVO;

    public Integer getBoundCustomers() {
        return boundCustomers;
    }

    public void setBoundCustomers(Integer boundCustomers) {
        this.boundCustomers = boundCustomers;
    }

    public Integer getInvitedVeeker() {
        return invitedVeeker;
    }

    public void setInvitedVeeker(Integer invitedVeeker) {
        this.invitedVeeker = invitedVeeker;
    }

    public Integer getPayNumber() {
        return payNumber;
    }

    public void setPayNumber(Integer payNumber) {
        this.payNumber = payNumber;
    }

    public Integer getSuccessOrderNumber() {
        return successOrderNumber;
    }

    public void setSuccessOrderNumber(Integer successOrderNumber) {
        this.successOrderNumber = successOrderNumber;
    }

    public Integer getSuccessTradingVolume() {
        return successTradingVolume;
    }

    public void setSuccessTradingVolume(Integer successTradingVolume) {
        this.successTradingVolume = successTradingVolume;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Double getExpenseNumber() {
        return expenseNumber;
    }

    public void setExpenseNumber(Double expenseNumber) {
        this.expenseNumber = expenseNumber;
    }

    public Double getSumOfConsumption() {
        return sumOfConsumption;
    }

    public void setSumOfConsumption(Double sumOfConsumption) {
        this.sumOfConsumption = sumOfConsumption;
    }

    public DistributionUserIncomeVO getDistributionUserIncomeVO() {
        return distributionUserIncomeVO;
    }

    public void setDistributionUserIncomeVO(DistributionUserIncomeVO distributionUserIncomeVO) {
        this.distributionUserIncomeVO = distributionUserIncomeVO;
    }

    public DistributionUserWalletVO getDistributionUserWalletVO() {
        return distributionUserWalletVO;
    }

    public void setDistributionUserWalletVO(DistributionUserWalletVO distributionUserWalletVO) {
        this.distributionUserWalletVO = distributionUserWalletVO;
    }

    @Override
    public String toString() {
        return "DistributionUserAchievementDataVO{" +
                "boundCustomers=" + boundCustomers +
                ", invitedVeeker=" + invitedVeeker +
                ", payNumber=" + payNumber +
                ", successOrderNumber=" + successOrderNumber +
                ", successTradingVolume=" + successTradingVolume +
                ", orderNumber=" + orderNumber +
                ", expenseNumber=" + expenseNumber +
                ", sumOfConsumption=" + sumOfConsumption +
                ", distributionUserIncomeVO=" + distributionUserIncomeVO +
                ", distributionUserWalletVO=" + distributionUserWalletVO +
                '}';
    }
}
