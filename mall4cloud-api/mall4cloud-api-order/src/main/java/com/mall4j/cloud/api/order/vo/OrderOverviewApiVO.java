package com.mall4j.cloud.api.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * @author lth
 */
@ApiModel("订单概况信息VO")
public class OrderOverviewApiVO {

    /**
     * 支付金额
     */
    @ApiModelProperty(value = "支付金额")
    private Double payActual;
    /**
     * 较前一日支付金额变化比率
     */
    @ApiModelProperty(value = "较前一日支付金额变化比率")
    private Double yesterdayPayActualRate;
    /**
     * 支付金额列表
     */
    @ApiModelProperty(value = "支付金额列表")
    private List<Double> payActualList;
    /**
     * 前一天支付金额列表
     */
    @ApiModelProperty(value = "前一天支付金额列表")
    private List<Double> yesterdayPayActualList;
    /**
     * 时间日期列表
     */
    @ApiModelProperty(value = "时间日期列表")
    private List<Date> payDateList;
    /**
     * 时间日期展示列表
     */
    @ApiModelProperty(value = "时间日期展示列表")
    private List<String> dateToStringList;
    /**
     * 支付客户数
     */
    @ApiModelProperty(value = "支付客户数")
    private Integer payUserCount;
    /**
     * 较前一日支付客户数变化比率
     */
    @ApiModelProperty(value = "较前一日支付客户数变化比率")
    private Double yesterdayPayUserRate;
    /**
     * 支付订单数
     */
    @ApiModelProperty(value = "支付订单数")
    private Integer payOrderCount;
    /**
     * 较前一日支付订单数变化比率
     */
    @ApiModelProperty(value = "较前一日支付订单数变化比率")
    private Double yesterdayPayOrderRate;
    /**
     * 退单数
     */
    @ApiModelProperty(value = "退单数")
    private Integer chargebackCount;
    /**
     * 退款金额
     */
    @ApiModelProperty(value = "退款金额")
    private Long refund;
    /**
     * 较前一日退款金额变化比率
     */
    @ApiModelProperty(value = "较前一日退款金额变化比率")
    private Double yesterdayRefundRate;
    /**
     * 客单价
     */
    @ApiModelProperty(value = "客单价")
    private Double onePrice;
    /**
     * 较前一日客单价变化比率
     */
    @ApiModelProperty(value = "较前一日客单价变化比率")
    private Double yesterdayOnePriceRate;
    /**
     * 时间展示数据
     */
    @ApiModelProperty(value = "时间展示数据")
    private String timeData;
    /**
     * 支付天数
     */
    @ApiModelProperty(value = "支付天数")
    private Date payDay;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String spuName;
    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "本月支付金额")
    private Double currentMonthPayActual;

    /**
     * 商品id
     */
    private Long spuId;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Double getCurrentMonthPayActual() {
        return currentMonthPayActual;
    }

    public void setCurrentMonthPayActual(Double currentMonthPayActual) {
        this.currentMonthPayActual = currentMonthPayActual;
    }

    public Double getYesterdayPayActualRate() {
        return yesterdayPayActualRate;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setYesterdayPayActualRate(Double yesterdayPayActualRate) {
        this.yesterdayPayActualRate = yesterdayPayActualRate;
    }

    public List<Double> getYesterdayPayActualList() {
        return yesterdayPayActualList;
    }

    public void setYesterdayPayActualList(List<Double> yesterdayPayActualList) {
        this.yesterdayPayActualList = yesterdayPayActualList;
    }

    public Double getYesterdayPayUserRate() {
        return yesterdayPayUserRate;
    }

    public void setYesterdayPayUserRate(Double yesterdayPayUserRate) {
        this.yesterdayPayUserRate = yesterdayPayUserRate;
    }

    public Double getYesterdayPayOrderRate() {
        return yesterdayPayOrderRate;
    }

    public void setYesterdayPayOrderRate(Double yesterdayPayOrderRate) {
        this.yesterdayPayOrderRate = yesterdayPayOrderRate;
    }

    public Double getYesterdayRefundRate() {
        return yesterdayRefundRate;
    }

    public void setYesterdayRefundRate(Double yesterdayRefundRate) {
        this.yesterdayRefundRate = yesterdayRefundRate;
    }

    public Double getYesterdayOnePriceRate() {
        return yesterdayOnePriceRate;
    }

    public void setYesterdayOnePriceRate(Double yesterdayOnePriceRate) {
        this.yesterdayOnePriceRate = yesterdayOnePriceRate;
    }

    public Double getPayActual() {
        return payActual;
    }

    public void setPayActual(Double payActual) {
        this.payActual = payActual;
    }

    public List<Double> getPayActualList() {
        return payActualList;
    }

    public void setPayActualList(List<Double> payActualList) {
        this.payActualList = payActualList;
    }

    public List<Date> getPayDateList() {
        return payDateList;
    }

    public void setPayDateList(List<Date> payDateList) {
        this.payDateList = payDateList;
    }

    public Integer getPayUserCount() {
        return payUserCount;
    }

    public void setPayUserCount(Integer payUserCount) {
        this.payUserCount = payUserCount;
    }

    public Integer getPayOrderCount() {
        return payOrderCount;
    }

    public void setPayOrderCount(Integer payOrderCount) {
        this.payOrderCount = payOrderCount;
    }

    public Integer getChargebackCount() {
        return chargebackCount;
    }

    public void setChargebackCount(Integer chargebackCount) {
        this.chargebackCount = chargebackCount;
    }

    public Long getRefund() {
        return refund;
    }

    public void setRefund(Long refund) {
        this.refund = refund;
    }

    public Double getOnePrice() {
        return onePrice;
    }

    public void setOnePrice(Double onePrice) {
        this.onePrice = onePrice;
    }

    public String getTimeData() {
        return timeData;
    }

    public void setTimeData(String timeData) {
        this.timeData = timeData;
    }

    public Date getPayDay() {
        return payDay;
    }

    public void setPayDay(Date payDay) {
        this.payDay = payDay;
    }

    public List<String> getDateToStringList() {
        return dateToStringList;
    }

    public void setDateToStringList(List<String> dateToStringList) {
        this.dateToStringList = dateToStringList;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    @Override
    public String toString() {
        return "OrderOverviewVO{" +
                "payActual=" + payActual +
                ", yesterdayPayActualRate=" + yesterdayPayActualRate +
                ", payActualList=" + payActualList +
                ", yesterdayPayActualList=" + yesterdayPayActualList +
                ", payDateList=" + payDateList +
                ", dateToStringList=" + dateToStringList +
                ", payUserCount=" + payUserCount +
                ", yesterdayPayUserRate=" + yesterdayPayUserRate +
                ", payOrderCount=" + payOrderCount +
                ", yesterdayPayOrderRate=" + yesterdayPayOrderRate +
                ", chargebackCount=" + chargebackCount +
                ", refund=" + refund +
                ", yesterdayRefundRate=" + yesterdayRefundRate +
                ", onePrice=" + onePrice +
                ", yesterdayOnePriceRate=" + yesterdayOnePriceRate +
                ", timeData='" + timeData + '\'' +
                ", payDay=" + payDay +
                ", spuName='" + spuName + '\'' +
                ", shopName='" + shopName + '\'' +
                ", currentMonthPayActual=" + currentMonthPayActual +
                ", spuId=" + spuId +
                '}';
    }
}
