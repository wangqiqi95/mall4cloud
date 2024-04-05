package com.mall4j.cloud.api.user.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author
 */
public class MemberDealTreadVO {

    private Long currentDay;

    @ApiModelProperty("新成交会员数")
    private Integer newPayMemberNum;
    @ApiModelProperty("老成交会员数")
    private Integer oldPayMemberNum;

    @ApiModelProperty("新支付订单数")
    private Integer newPayOrderNum;
    @ApiModelProperty("老支付订单数")
    private Integer oldPayOrderNum;

    @ApiModelProperty("新客单价")
    private Double newPricePerMember;
    @ApiModelProperty("老客单价")
    private Double oldPricePerMember;

    @ApiModelProperty("新支付金额")
    private Double newPayAmount;
    @ApiModelProperty("老支付金额")
    private Double oldPayAmount;

    public Long getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(Long currentDay) {
        this.currentDay = currentDay;
    }

    public Integer getNewPayMemberNum() {
        return newPayMemberNum;
    }

    public void setNewPayMemberNum(Integer newPayMemberNum) {
        this.newPayMemberNum = newPayMemberNum;
    }

    public Integer getOldPayMemberNum() {
        return oldPayMemberNum;
    }

    public void setOldPayMemberNum(Integer oldPayMemberNum) {
        this.oldPayMemberNum = oldPayMemberNum;
    }

    public Integer getNewPayOrderNum() {
        return newPayOrderNum;
    }

    public void setNewPayOrderNum(Integer newPayOrderNum) {
        this.newPayOrderNum = newPayOrderNum;
    }

    public Integer getOldPayOrderNum() {
        return oldPayOrderNum;
    }

    public void setOldPayOrderNum(Integer oldPayOrderNum) {
        this.oldPayOrderNum = oldPayOrderNum;
    }

    public Double getNewPricePerMember() {
        return newPricePerMember;
    }

    public void setNewPricePerMember(Double newPricePerMember) {
        this.newPricePerMember = newPricePerMember;
    }

    public Double getOldPricePerMember() {
        return oldPricePerMember;
    }

    public void setOldPricePerMember(Double oldPricePerMember) {
        this.oldPricePerMember = oldPricePerMember;
    }

    public Double getNewPayAmount() {
        return newPayAmount;
    }

    public void setNewPayAmount(Double newPayAmount) {
        this.newPayAmount = newPayAmount;
    }

    public Double getOldPayAmount() {
        return oldPayAmount;
    }

    public void setOldPayAmount(Double oldPayAmount) {
        this.oldPayAmount = oldPayAmount;
    }

    @Override
    public String toString() {
        return "MemberDealTreadVO{" +
                "currentDay=" + currentDay +
                ", newPayMemberNum=" + newPayMemberNum +
                ", oldPayMemberNum=" + oldPayMemberNum +
                ", newPayOrderNum=" + newPayOrderNum +
                ", oldPayOrderNum=" + oldPayOrderNum +
                ", newPricePerMember=" + newPricePerMember +
                ", oldPricePerMember=" + oldPricePerMember +
                ", newPayAmount=" + newPayAmount +
                ", oldPayAmount=" + oldPayAmount +
                '}';
    }
}
