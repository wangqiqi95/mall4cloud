package com.mall4j.cloud.order.dto.app;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 申请退款参数
 * @author FrozenWatermelon
 */
public class OrderRefundPageDTO {

    @ApiModelProperty(value = "订单号")
    private Long orderId;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty(value = "退款单类型（1:整单退款,2:单个物品退款）")
    private Integer refundType;

    @ApiModelProperty(value = "申请类型:1,仅退款,2退款退货")
    private Integer applyType;

    @ApiModelProperty(value = "手机号码（默认当前订单手机号码）")
    private String buyerMobile;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "退款单号")
    private Long refundId;

    @ApiModelProperty(value = "退款单号编号")
    private String refundNumber;

    @ApiModelProperty(value = "退款申请开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    @ApiModelProperty(value = "退款申请结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "退款成功开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date refundBeginTime;

    @ApiModelProperty(value = "退款成功结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date refundEndTime;

    @ApiModelProperty(value = "处理退款状态:(1.买家申请 2.卖家接受 3.买家发货 4.卖家收货 5.退款成功  -1.退款关闭)")
    private Integer returnMoneySts;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "订单类型 1团购订单 2秒杀订单 3积分订单")
    private Integer orderType;

    @ApiModelProperty(value = "退货单物流单号")
    private String deliveryNo;

    @ApiModelProperty(value = "是否分销订单0否1是")
    private Integer isDistributionOrder = 0;

    @ApiModelProperty(value = "申请来源 0-小程序 1-后台发起")
    private Integer applySource;

    @ApiModelProperty(value = "订单来源 0 普通订单 1 小程序直播 2 视频号3.0")
    private Integer orderSource;

    @ApiModelProperty(value = "视频号售后单号")
    private Long aftersaleId;

    @ApiModelProperty(value = "纠纷单查询，1视频号纠纷单，0普通退款订单")
    private Integer disputable;

    public Long getAftersaleId() {
        return aftersaleId;
    }

    public void setAftersaleId(Long aftersaleId) {
        this.aftersaleId = aftersaleId;
    }

    public Integer getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(Integer orderSource) {
        this.orderSource = orderSource;
    }

    public Integer getIsDistributionOrder() {
        return isDistributionOrder;
    }

    public void setIsDistributionOrder(Integer isDistributionOrder) {
        this.isDistributionOrder = isDistributionOrder;
    }

    public Date getRefundBeginTime() {
        return refundBeginTime;
    }

    public void setRefundBeginTime(Date refundBeginTime) {
        this.refundBeginTime = refundBeginTime;
    }

    public Date getRefundEndTime() {
        return refundEndTime;
    }

    public void setRefundEndTime(Date refundEndTime) {
        this.refundEndTime = refundEndTime;
    }

    public String getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getRefundType() {
        return refundType;
    }

    public void setRefundType(Integer refundType) {
        this.refundType = refundType;
    }

    public Integer getApplyType() {
        return applyType;
    }

    public void setApplyType(Integer applyType) {
        this.applyType = applyType;
    }

    public String getBuyerMobile() {
        return buyerMobile;
    }

    public void setBuyerMobile(String buyerMobile) {
        this.buyerMobile = buyerMobile;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getRefundId() {
        return refundId;
    }

    public void setRefundId(Long refundId) {
        this.refundId = refundId;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getReturnMoneySts() {
        return returnMoneySts;
    }

    public void setReturnMoneySts(Integer returnMoneySts) {
        this.returnMoneySts = returnMoneySts;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getRefundNumber() {
        return refundNumber;
    }

    public void setRefundNumber(String refundNumber) {
        this.refundNumber = refundNumber;
    }

    public Integer getApplySource() {
        return applySource;
    }

    public void setApplySource(Integer applySource) {
        this.applySource = applySource;
    }

    public Integer getDisputable() {
        return disputable;
    }

    public void setDisputable(Integer disputable) {
        this.disputable = disputable;
    }

    @Override
    public String toString() {
        return "OrderRefundPageDTO{" +
                "orderId=" + orderId +
                ", orderNumber='" + orderNumber + '\'' +
                ", refundType=" + refundType +
                ", applyType=" + applyType +
                ", buyerMobile='" + buyerMobile + '\'' +
                ", userId=" + userId +
                ", shopId=" + shopId +
                ", refundId=" + refundId +
                ", refundNumber='" + refundNumber + '\'' +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", refundBeginTime=" + refundBeginTime +
                ", refundEndTime=" + refundEndTime +
                ", returnMoneySts=" + returnMoneySts +
                ", shopName='" + shopName + '\'' +
                ", orderType=" + orderType +
                ", deliveryNo='" + deliveryNo + '\'' +
                ", isDistributionOrder=" + isDistributionOrder +
                ", applySource=" + applySource +
                ", disputable=" + disputable +
                '}';
    }
}
