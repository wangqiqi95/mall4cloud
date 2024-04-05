package com.mall4j.cloud.api.order.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Pineapple
 * @date 2021/5/21 16:06
 */
public class OrderProdEffectRespVO {

    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private Long spuId;
    /**
     * 下单人数
     */
    @ApiModelProperty("下单人数")
    private Integer placeOrderPerson;
    /**
     * 支付人数
     */
    @ApiModelProperty("支付人数")
    private Integer payPerson;
    /**
     * 下单商品件数
     */
    @ApiModelProperty("下单商品件数")
    private Integer placeOrderNum;
    /**
     * 支付商品件数
     */
    @ApiModelProperty("支付商品件数")
    private Integer payNum;
    /**
     * 商品下单金额
     */
    @ApiModelProperty("商品下单金额")
    private Double placeOrderAmount;
    /**
     * 商品支付金额
     */
    @ApiModelProperty("商品支付金额")
    private Double payAmount;

    /**
     * 支付成功订单数，计算退款率使用
     */
    private Integer payOrderNum;

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Integer getPlaceOrderPerson() {
        return placeOrderPerson;
    }

    public void setPlaceOrderPerson(Integer placeOrderPerson) {
        this.placeOrderPerson = placeOrderPerson;
    }

    public Integer getPayPerson() {
        return payPerson;
    }

    public void setPayPerson(Integer payPerson) {
        this.payPerson = payPerson;
    }

    public Integer getPlaceOrderNum() {
        return placeOrderNum;
    }

    public void setPlaceOrderNum(Integer placeOrderNum) {
        this.placeOrderNum = placeOrderNum;
    }

    public Integer getPayNum() {
        return payNum;
    }

    public void setPayNum(Integer payNum) {
        this.payNum = payNum;
    }

    public Double getPlaceOrderAmount() {
        return placeOrderAmount;
    }

    public void setPlaceOrderAmount(Double placeOrderAmount) {
        this.placeOrderAmount = placeOrderAmount;
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public Integer getPayOrderNum() {
        return payOrderNum;
    }

    public void setPayOrderNum(Integer payOrderNum) {
        this.payOrderNum = payOrderNum;
    }

    @Override
    public String toString() {
        return "OrderProdEffectRespVO{" +
                "spuId=" + spuId +
                ", placeOrderPerson=" + placeOrderPerson +
                ", payPerson=" + payPerson +
                ", placeOrderNum=" + placeOrderNum +
                ", payNum=" + payNum +
                ", placeOrderAmount=" + placeOrderAmount +
                ", payAmount=" + payAmount +
                ", payOrderNum=" + payOrderNum +
                '}';
    }
}