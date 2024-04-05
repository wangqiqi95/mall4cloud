package com.mall4j.cloud.api.user.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author lhd
 */
public class MemberOverviewVO {

    public MemberOverviewVO(){
        this.totalMember = 0;
        this.totalMemberRate = 0.0;
        this.newMember = 0;
        this.newMemberRate = 0.0;
        this.payMember = 0;
        this.payMemberRate = 0.0;
        this.couponMember = 0;
        this.couponMemberRate = 0.0;
        this.memberPayAmount = 0.0;
        this.memberPayAmountRate = 0.0;
        this.memberPayOrder = 0;
        this.memberPayOrderRate = 0.0;
        this.memberSingleAmount = 0.0;
        this.memberSingleAmountRate = 0.0;
    }
    private Long currentDay;

    @ApiModelProperty("累积会员数")
    private Integer totalMember;

    @ApiModelProperty("新增会员数")
    private Integer newMember;

    @ApiModelProperty("付费会员数")
    private Integer paidMember;

    @ApiModelProperty("支付会员数")
    private Integer payMember;

    @ApiModelProperty("领券会员数")
    private Integer couponMember;

    @ApiModelProperty("会员支付金额")
    private Double memberPayAmount;

    @ApiModelProperty("会员支付订单数")
    private Integer memberPayOrder;

    @ApiModelProperty("会员客单价")
    private Double memberSingleAmount;

    @ApiModelProperty("累积会员与之前的  >0 上升/ <0下降 率,")
    private Double totalMemberRate;

    @ApiModelProperty("新增会员会员与之前的 上升/下降 率")
    private Double newMemberRate;

    @ApiModelProperty("支付会员数，变化率")
    private Double payMemberRate;

    @ApiModelProperty("领券会员数，变化率")
    private Double couponMemberRate;

    @ApiModelProperty("会员支付金额，变化率")
    private Double memberPayAmountRate;

    @ApiModelProperty("会员支付订单数, 变化率")
    private Double memberPayOrderRate;

    @ApiModelProperty("会员客单价, 变化率")
    private Double memberSingleAmountRate;

    @ApiModelProperty("符合条件的userIds")
    private List<Long> userIds;

    @ApiModelProperty("储值会员数")
    private Integer storedValue;

    public Double getTotalMemberRate() {
        return totalMemberRate;
    }

    public void setTotalMemberRate(Double totalMemberRate) {
        this.totalMemberRate = totalMemberRate;
    }

    public Double getNewMemberRate() {
        return newMemberRate;
    }

    public void setNewMemberRate(Double newMemberRate) {
        this.newMemberRate = newMemberRate;
    }

    public Double getPayMemberRate() {
        return payMemberRate;
    }

    public void setPayMemberRate(Double payMemberRate) {
        this.payMemberRate = payMemberRate;
    }

    public Double getCouponMemberRate() {
        return couponMemberRate;
    }

    public void setCouponMemberRate(Double couponMemberRate) {
        this.couponMemberRate = couponMemberRate;
    }

    public Double getMemberPayAmountRate() {
        return memberPayAmountRate;
    }

    public void setMemberPayAmountRate(Double memberPayAmountRate) {
        this.memberPayAmountRate = memberPayAmountRate;
    }

    public Double getMemberPayOrderRate() {
        return memberPayOrderRate;
    }

    public void setMemberPayOrderRate(Double memberPayOrderRate) {
        this.memberPayOrderRate = memberPayOrderRate;
    }

    public Double getMemberSingleAmountRate() {
        return memberSingleAmountRate;
    }

    public void setMemberSingleAmountRate(Double memberSingleAmountRate) {
        this.memberSingleAmountRate = memberSingleAmountRate;
    }

    public Integer getPaidMember() {
        return paidMember;
    }

    public void setPaidMember(Integer paidMember) {
        this.paidMember = paidMember;
    }

    public Integer getStoredValue() {
        return storedValue;
    }

    public void setStoredValue(Integer storedValue) {
        this.storedValue = storedValue;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public Long getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(Long currentDay) {
        this.currentDay = currentDay;
    }

    public Integer getTotalMember() {
        return totalMember;
    }

    public void setTotalMember(Integer totalMember) {
        this.totalMember = totalMember;
    }

    public Integer getNewMember() {
        return newMember;
    }

    public void setNewMember(Integer newMember) {
        this.newMember = newMember;
    }

    public Integer getPayMember() {
        return payMember;
    }

    public void setPayMember(Integer payMember) {
        this.payMember = payMember;
    }

    public Integer getCouponMember() {
        return couponMember;
    }

    public void setCouponMember(Integer couponMember) {
        this.couponMember = couponMember;
    }

    public Double getMemberPayAmount() {
        return memberPayAmount;
    }

    public void setMemberPayAmount(Double memberPayAmount) {
        this.memberPayAmount = memberPayAmount;
    }

    public Integer getMemberPayOrder() {
        return memberPayOrder;
    }

    public void setMemberPayOrder(Integer memberPayOrder) {
        this.memberPayOrder = memberPayOrder;
    }

    public Double getMemberSingleAmount() {
        return memberSingleAmount;
    }

    public void setMemberSingleAmount(Double memberSingleAmount) {
        this.memberSingleAmount = memberSingleAmount;
    }

    @Override
    public String toString() {
        return "MemberOverviewVO{" +
                "currentDay=" + currentDay +
                ", totalMember=" + totalMember +
                ", newMember=" + newMember +
                ", paidMember=" + paidMember +
                ", payMember=" + payMember +
                ", couponMember=" + couponMember +
                ", memberPayAmount=" + memberPayAmount +
                ", memberPayOrder=" + memberPayOrder +
                ", memberSingleAmount=" + memberSingleAmount +
                ", totalMemberRate=" + totalMemberRate +
                ", newMemberRate=" + newMemberRate +
                ", payMemberRate=" + payMemberRate +
                ", couponMemberRate=" + couponMemberRate +
                ", memberPayAmountRate=" + memberPayAmountRate +
                ", memberPayOrderRate=" + memberPayOrderRate +
                ", memberSingleAmountRate=" + memberSingleAmountRate +
                ", userIds=" + userIds +
                ", storedValue=" + storedValue +
                '}';
    }
}
