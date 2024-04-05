package com.mall4j.cloud.api.user.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author
 */
public class MemberDealVO {

    @ApiModelProperty("成交会员数")
    private Integer payMemberNum;

    @ApiModelProperty("成交会员数占比")
    private Double payMemberNumRate;

    @ApiModelProperty("支付订单数")
    private Integer payOrderNum;

    @ApiModelProperty("客单价")
    private Double pricePerMember;

    @ApiModelProperty("支付金额")
    private Double payAmount;

    @ApiModelProperty("支付金额占比")
    private Double payAmountRate;

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

    public Double getPricePerMember() {
        return pricePerMember;
    }

    public void setPricePerMember(Double pricePerMember) {
        this.pricePerMember = pricePerMember;
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

    @Override
    public String toString() {
        return "MemberDealVO{" +
                "payMemberNum=" + payMemberNum +
                ", payMemberNumRate=" + payMemberNumRate +
                ", payOrderNum=" + payOrderNum +
                ", pricePerMember=" + pricePerMember +
                ", payAmount=" + payAmount +
                ", payAmountRate=" + payAmountRate +
                '}';
    }
}
