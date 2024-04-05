package com.mall4j.cloud.api.order.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Pineapple
 * @date 2021/5/21 16:06
 */
public class OrderRefundProdEffectRespVO {
    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private Long spuId;
    /**
     * 申请退款订单数
     */
    @ApiModelProperty("申请退款订单数")
    private Integer refundNum;

    /**
     * 申请退款人数
     */
    @ApiModelProperty("申请退款人数")
    private Integer refundPerson;
    /**
     * 成功退款订单数
     */
    @ApiModelProperty("成功退款订单数")
    private Integer refundSuccessNum;
    /**
     * 成功退款人数
     */
    @ApiModelProperty("成功退款人数")
    private Integer refundSuccessPerson;
    /**
     * 成功退款金额
     */
    @ApiModelProperty("成功退款金额")
    private Double refundSuccessAmount;
    /**
     * 退款率
     */
    @ApiModelProperty("退款率")
    private Double refundSuccessRate;

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Integer getRefundNum() {
        return refundNum;
    }

    public void setRefundNum(Integer refundNum) {
        this.refundNum = refundNum;
    }

    public Integer getRefundPerson() {
        return refundPerson;
    }

    public void setRefundPerson(Integer refundPerson) {
        this.refundPerson = refundPerson;
    }

    public Integer getRefundSuccessNum() {
        return refundSuccessNum;
    }

    public void setRefundSuccessNum(Integer refundSuccessNum) {
        this.refundSuccessNum = refundSuccessNum;
    }

    public Integer getRefundSuccessPerson() {
        return refundSuccessPerson;
    }

    public void setRefundSuccessPerson(Integer refundSuccessPerson) {
        this.refundSuccessPerson = refundSuccessPerson;
    }

    public Double getRefundSuccessAmount() {
        return refundSuccessAmount;
    }

    public void setRefundSuccessAmount(Double refundSuccessAmount) {
        this.refundSuccessAmount = refundSuccessAmount;
    }

    public Double getRefundSuccessRate() {
        return refundSuccessRate;
    }

    public void setRefundSuccessRate(Double refundSuccessRate) {
        this.refundSuccessRate = refundSuccessRate;
    }

    @Override
    public String toString() {
        return "OrderRefundProdEffectRespVO{" +
                ", spuId"+ spuId +
                ", refundNum=" + refundNum +
                ", refundPerson=" + refundPerson +
                ", refundSuccessNum=" + refundSuccessNum +
                ", refundSuccessPerson=" + refundSuccessPerson +
                ", refundSuccessAmount=" + refundSuccessAmount +
                ", refundSuccessRate=" + refundSuccessRate +
                '}';
    }
}