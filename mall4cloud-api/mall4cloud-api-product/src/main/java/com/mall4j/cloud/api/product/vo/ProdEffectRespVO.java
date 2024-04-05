package com.mall4j.cloud.api.product.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Pineapple
 * @date 2021/5/21 16:06
 */
public class ProdEffectRespVO {
    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private Long spuId;
    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String spuName;
    /**
     * 商品图片
     */
    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty("商品图片")
    private String spuUrl;
    /**
     * 商品价格
     */
    @ApiModelProperty("商品价格")
    private Long price;
    /**
     * 曝光次数
     */
    @ApiModelProperty("曝光次数")
    private Integer expose;
    /**
     * 曝光人数
     */
    @ApiModelProperty("曝光人数")
    private Integer exposePersonNum;
    /**
     * 加购人数
     */
    @ApiModelProperty("加购人数")
    private Integer addCartPerson;
    /**
     * 加购件数
     */
    @ApiModelProperty("加购件数")
    private Integer addCart;
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

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public String getSpuUrl() {
        return spuUrl;
    }

    public void setSpuUrl(String spuUrl) {
        this.spuUrl = spuUrl;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getExpose() {
        return expose;
    }

    public void setExpose(Integer expose) {
        this.expose = expose;
    }

    public Integer getExposePersonNum() {
        return exposePersonNum;
    }

    public void setExposePersonNum(Integer exposePersonNum) {
        this.exposePersonNum = exposePersonNum;
    }

    public Integer getAddCartPerson() {
        return addCartPerson;
    }

    public void setAddCartPerson(Integer addCartPerson) {
        this.addCartPerson = addCartPerson;
    }

    public Integer getAddCart() {
        return addCart;
    }

    public void setAddCart(Integer addCart) {
        this.addCart = addCart;
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
        return "ProdEffectRespVO{" +
                "spuId=" + spuId +
                ", spuName='" + spuName + '\'' +
                ", spuUrl='" + spuUrl + '\'' +
                ", price=" + price +
                ", expose=" + expose +
                ", exposePersonNum=" + exposePersonNum +
                ", addCartPerson=" + addCartPerson +
                ", addCart=" + addCart +
                ", placeOrderPerson=" + placeOrderPerson +
                ", payPerson=" + payPerson +
                ", placeOrderNum=" + placeOrderNum +
                ", payNum=" + payNum +
                ", placeOrderAmount=" + placeOrderAmount +
                ", payAmount=" + payAmount +
                ", refundNum=" + refundNum +
                ", refundPerson=" + refundPerson +
                ", refundSuccessNum=" + refundSuccessNum +
                ", refundSuccessPerson=" + refundSuccessPerson +
                ", refundSuccessAmount=" + refundSuccessAmount +
                ", refundSuccessRate=" + refundSuccessRate +
                '}';
    }
}