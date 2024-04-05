package com.mall4j.cloud.order.vo;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author lth
 */
public class OrderRefundStatisticsVO {
    /**
     * 退款金额
     */
    @ApiModelProperty(value = "退款金额")
    private Long payActualTotal;
    /**
     * 退款成功订单数
     */
    @ApiModelProperty(value = "退款成功订单数")
    private Integer refundCount;
    /**
     * 总订单数
     */
    @ApiModelProperty(value = "总订单数")
    private Integer totalOrderCount;
    /**
     * 退款原因
     */
    @ApiModelProperty(value = "退款原因")
    private String buyerReason;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private Long spuId;
    /**
     * 退款商品名称
     */
    @ApiModelProperty(value = "退款商品名称")
    private String refundProdName;
    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;
    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String shopName;
    /**
     * 退款率
     */
    @ApiModelProperty(value = "退款率")
    private Double refundRate;
    /**
     * 退款日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "退款日期")
    private Date refundDate;
    /**
     * 退款日期
     */
    @ApiModelProperty(value = "退款日期")
    private String refundDateToString;

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getPayActualTotal() {
        return payActualTotal;
    }

    public void setPayActualTotal(Long payActualTotal) {
        this.payActualTotal = payActualTotal;
    }

    public Integer getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(Integer refundCount) {
        this.refundCount = refundCount;
    }

    public String getBuyerReason() {
        return buyerReason;
    }

    public void setBuyerReason(String buyerReason) {
        this.buyerReason = buyerReason;
    }

    public String getRefundProdName() {
        return refundProdName;
    }

    public void setRefundProdName(String refundProdName) {
        this.refundProdName = refundProdName;
    }

    public Date getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(Date refundDate) {
        this.refundDate = refundDate;
    }

    public String getRefundDateToString() {
        return refundDateToString;
    }

    public void setRefundDateToString(String refundDateToString) {
        this.refundDateToString = refundDateToString;
    }

    public Integer getTotalOrderCount() {
        return totalOrderCount;
    }

    public void setTotalOrderCount(Integer totalOrderCount) {
        this.totalOrderCount = totalOrderCount;
    }

    public Double getRefundRate() {
        return refundRate;
    }

    public void setRefundRate(Double refundRate) {
        this.refundRate = refundRate;
    }

    @Override
    public String toString() {
        return "OrderRefundStatisticsVO{" +
                "payActualTotal=" + payActualTotal +
                ", refundCount=" + refundCount +
                ", totalOrderCount=" + totalOrderCount +
                ", buyerReason='" + buyerReason + '\'' +
                ", spuId=" + spuId +
                ", refundProdName='" + refundProdName + '\'' +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", refundRate=" + refundRate +
                ", refundDate=" + refundDate +
                ", refundDateToString='" + refundDateToString + '\'' +
                '}';
    }
}
