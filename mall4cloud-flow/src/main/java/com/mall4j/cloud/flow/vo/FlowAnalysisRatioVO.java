package com.mall4j.cloud.flow.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 流量数据比例
 *
 * @author YXF
 * @date 2021-06-02
 */
public class FlowAnalysisRatioVO {

    @ApiModelProperty("访客数比例")
    private Double userNumsRatio;

    @ApiModelProperty("新访客数比例")
    private Double newUserNumsRatio;

    @ApiModelProperty("跳失率比例")
    private Double lossUserRatio;

    @ApiModelProperty("访问量比例")
    private Double visitNumsRatio;

    @ApiModelProperty("人均访问量比例")
    private Double averageVisitNumsRatio;

    @ApiModelProperty("加购人数比例")
    private Double plusShopCartUserRatio;

    @ApiModelProperty("访问-下单比例")
    private Double placeOrderRatio;

    @ApiModelProperty("下单用户比例")
    private Double placeOrderUserRatio;

    @ApiModelProperty("访问-支付比例")
    private Double payRatio;

    @ApiModelProperty("支付用户比例")
    private Double payUserRatio;

    public Double getUserNumsRatio() {
        return userNumsRatio;
    }

    public void setUserNumsRatio(Double userNumsRatio) {
        this.userNumsRatio = userNumsRatio;
    }

    public Double getNewUserNumsRatio() {
        return newUserNumsRatio;
    }

    public void setNewUserNumsRatio(Double newUserNumsRatio) {
        this.newUserNumsRatio = newUserNumsRatio;
    }

    public Double getLossUserRatio() {
        return lossUserRatio;
    }

    public void setLossUserRatio(Double lossUserRatio) {
        this.lossUserRatio = lossUserRatio;
    }

    public Double getVisitNumsRatio() {
        return visitNumsRatio;
    }

    public void setVisitNumsRatio(Double visitNumsRatio) {
        this.visitNumsRatio = visitNumsRatio;
    }

    public Double getAverageVisitNumsRatio() {
        return averageVisitNumsRatio;
    }

    public void setAverageVisitNumsRatio(Double averageVisitNumsRatio) {
        this.averageVisitNumsRatio = averageVisitNumsRatio;
    }

    public Double getPlusShopCartUserRatio() {
        return plusShopCartUserRatio;
    }

    public void setPlusShopCartUserRatio(Double plusShopCartUserRatio) {
        this.plusShopCartUserRatio = plusShopCartUserRatio;
    }

    public Double getPlaceOrderRatio() {
        return placeOrderRatio;
    }

    public void setPlaceOrderRatio(Double placeOrderRatio) {
        this.placeOrderRatio = placeOrderRatio;
    }

    public Double getPlaceOrderUserRatio() {
        return placeOrderUserRatio;
    }

    public void setPlaceOrderUserRatio(Double placeOrderUserRatio) {
        this.placeOrderUserRatio = placeOrderUserRatio;
    }

    public Double getPayRatio() {
        return payRatio;
    }

    public void setPayRatio(Double payRatio) {
        this.payRatio = payRatio;
    }

    public Double getPayUserRatio() {
        return payUserRatio;
    }

    public void setPayUserRatio(Double payUserRatio) {
        this.payUserRatio = payUserRatio;
    }

    @Override
    public String toString() {
        return "FlowAnalysisRatioVO{" +
                "userNumsRatio=" + userNumsRatio +
                ", newUserNumsRatio=" + newUserNumsRatio +
                ", lossUserRatio=" + lossUserRatio +
                ", visitNumsRatio=" + visitNumsRatio +
                ", averageVisitNumsRatio=" + averageVisitNumsRatio +
                ", plusShopCartUserRatio=" + plusShopCartUserRatio +
                ", placeOrderRatio=" + placeOrderRatio +
                ", placeOrderUserRatio=" + placeOrderUserRatio +
                ", payRatio=" + payRatio +
                ", payUserRatio=" + payUserRatio +
                '}';
    }
}
