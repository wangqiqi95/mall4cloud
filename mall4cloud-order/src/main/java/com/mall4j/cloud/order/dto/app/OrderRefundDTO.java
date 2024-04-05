package com.mall4j.cloud.order.dto.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 申请退款参数
 * @author FrozenWatermelon
 */
@ApiModel("app-申请退款参数")
public class OrderRefundDTO {

    @ApiModelProperty(value = "订单号", required = true)
    @NotNull(message = "订单号不能为空")
    private Long orderId;

    @ApiModelProperty(value = "退款单类型（1:整单退款,2:单个物品退款）", required = true)
    @NotNull(message = "退款单类型不能为空")
    private Integer refundType;

    @ApiModelProperty(value = "订单项ID(0:为全部订单项)", required = true)
    private Long orderItemId;

    @ApiModelProperty(value = "退货数量(0:为全部订单项)", required = true)
    private Integer refundCount;

    @ApiModelProperty(value = "申请类型:1,仅退款,2退款退货", required = true)
    @NotNull(message = "申请类型不能为空")
    private Integer applyType;

    @ApiModelProperty(value = "是否接收到商品(1:已收到,0:未收到)", required = true)
    @NotNull(message = "是否接收到商品不能为空")
    private Integer isReceived;

    @ApiModelProperty(value = "申请原因(下拉选择)")
    private Integer buyerReason;

    @ApiModelProperty(value = "退款金额", required = true)
    @NotNull(message = "退款金额不能为空")
    private Long refundAmount;

    @ApiModelProperty(value = "手机号码（默认当前订单手机号码）", required = true)
//    @NotEmpty(message = "手机号码不能为空")
    private String buyerMobile;

    @ApiModelProperty(value = "申请说明", required = true)
    private String buyerDesc;

    @ApiModelProperty(value = "文件凭证(逗号隔开)")
    private String imgUrls;

    @ApiModelProperty(value = "文件凭证(逗号隔开)")
    private Long userId;

    @ApiModelProperty(value = "运费退款金额")
    private Long refundFreightAmount;

    public Long getRefundFreightAmount() {
        return refundFreightAmount;
    }

    public void setRefundFreightAmount(Long refundFreightAmount) {
        this.refundFreightAmount = refundFreightAmount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Integer getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(Integer refundCount) {
        this.refundCount = refundCount;
    }

    public Integer getApplyType() {
        return applyType;
    }

    public void setApplyType(Integer applyType) {
        this.applyType = applyType;
    }

    public Integer getReceived() {
        return isReceived;
    }

    public void setReceived(Integer received) {
        isReceived = received;
    }

    public Integer getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(Integer isReceived) {
        this.isReceived = isReceived;
    }

    public Integer getBuyerReason() {
        return buyerReason;
    }

    public void setBuyerReason(Integer buyerReason) {
        this.buyerReason = buyerReason;
    }

    public Long getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Long refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getBuyerMobile() {
        return buyerMobile;
    }

    public void setBuyerMobile(String buyerMobile) {
        this.buyerMobile = buyerMobile;
    }

    public String getBuyerDesc() {
        return buyerDesc;
    }

    public void setBuyerDesc(String buyerDesc) {
        this.buyerDesc = buyerDesc;
    }

    public String getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(String imgUrls) {
        this.imgUrls = imgUrls;
    }

    @Override
    public String toString() {
        return "OrderRefundDTO{" +
                "orderId=" + orderId +
                ", refundType=" + refundType +
                ", orderItemId=" + orderItemId +
                ", refundCount=" + refundCount +
                ", applyType=" + applyType +
                ", isReceived=" + isReceived +
                ", buyerReason='" + buyerReason + '\'' +
                ", refundAmount=" + refundAmount +
                ", buyerMobile='" + buyerMobile + '\'' +
                ", buyerDesc='" + buyerDesc + '\'' +
                ", imgUrls='" + imgUrls + '\'' +
                '}';
    }
}
