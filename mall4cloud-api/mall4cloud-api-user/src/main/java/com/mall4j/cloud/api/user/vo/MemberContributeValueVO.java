package com.mall4j.cloud.api.user.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author
 */
public class MemberContributeValueVO {

    public MemberContributeValueVO(){
        this.payOrderNum = 0;
        this.totalMember = 0;
        this.totalMemberRate = 0.00;
        this.payMemberNum = 0;
        this.payMemberNumRate = 0.00;
        this.payAmount = 0.00;
        this.payAmountRate = 0.00;
        this.pricePerMember = 0.00;
        this.frequencyOfConsume = 0.00;
    }
    @ApiModelProperty("累积会员数")
    private Integer totalMember;
    @ApiModelProperty("累积会员数占比")
    private Double totalMemberRate;

    @ApiModelProperty("成交会员数")
    private Integer payMemberNum;
    @ApiModelProperty("成交会员数占比")
    private Double payMemberNumRate;

    @ApiModelProperty("支付订单数")
    private Integer payOrderNum;

    @ApiModelProperty("支付金额")
    private Double payAmount;
    @ApiModelProperty("支付金额占比")
    private Double payAmountRate;

    @ApiModelProperty("客单价")
    private Double pricePerMember;

    @ApiModelProperty("人均消费频次")
    private Double frequencyOfConsume;

    public Integer getTotalMember() {
        return totalMember;
    }

    public void setTotalMember(Integer totalMember) {
        this.totalMember = totalMember;
    }

    public Double getTotalMemberRate() {
        return totalMemberRate;
    }

    public void setTotalMemberRate(Double totalMemberRate) {
        this.totalMemberRate = totalMemberRate;
    }

    public Integer getPayMemberNum() {
        return payMemberNum;
    }

    public void setPayMemberNum(Integer payMemberNum) {
        this.payMemberNum = payMemberNum;
    }

    public Double getPayMemberNumRate() {
        return payMemberNumRate;
    }

    public void setPayMemberNumRate(Double payMemberNumRate) {
        this.payMemberNumRate = payMemberNumRate;
    }

    public Integer getPayOrderNum() {
        return payOrderNum;
    }

    public void setPayOrderNum(Integer payOrderNum) {
        this.payOrderNum = payOrderNum;
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public Double getPayAmountRate() {
        return payAmountRate;
    }

    public void setPayAmountRate(Double payAmountRate) {
        this.payAmountRate = payAmountRate;
    }

    public Double getPricePerMember() {
        return pricePerMember;
    }

    public void setPricePerMember(Double pricePerMember) {
        this.pricePerMember = pricePerMember;
    }

    public Double getFrequencyOfConsume() {
        return frequencyOfConsume;
    }

    public void setFrequencyOfConsume(Double frequencyOfConsume) {
        this.frequencyOfConsume = frequencyOfConsume;
    }

    @Override
    public String toString() {
        return "MemberContributeValueVO{" +
                "totalMember=" + totalMember +
                ", totalMemberRate=" + totalMemberRate +
                ", payMemberNum=" + payMemberNum +
                ", payMemberNumRate=" + payMemberNumRate +
                ", payOrderNum=" + payOrderNum +
                ", payAmount=" + payAmount +
                ", payAmountRate=" + payAmountRate +
                ", pricePerMember=" + pricePerMember +
                ", frequencyOfConsume=" + frequencyOfConsume +
                '}';
    }
}
