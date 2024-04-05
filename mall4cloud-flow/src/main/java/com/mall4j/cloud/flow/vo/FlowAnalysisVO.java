package com.mall4j.cloud.flow.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 流量分析—页面数据统计表
 *
 * @author YXF
 * @date 2021-06-02
 */
public class FlowAnalysisVO {

    @ApiModelProperty("日期时间")
    private String dateTime;

    @ApiModelProperty("UV价值 (支付金额／访客数)")
    private Long uvAmount;

    @ApiModelProperty("客单价")
    private Long customerAmount;

    @ApiModelProperty("流失用户")
    private Integer lossUser;

    @ApiModelProperty("跳失率")
    private Double lossUserRate;

    @ApiModelProperty("加购数量")
    private Integer plusShopCart;

    @ApiModelProperty("加购人数")
    private Integer plusShopCartUser;

    @ApiModelProperty("下单金额")
    private Long placeOrderAmount;

    @ApiModelProperty("下单用户")
    private Integer placeOrderUser;

    @ApiModelProperty("访问-下单率")
    private Double placeOrderRate;

    @ApiModelProperty("支付金额")
    private Long payAmount;

    @ApiModelProperty("支付用户")
    private Integer payUser;

    @ApiModelProperty("访问-支付率")
    private Double payRate;

    @ApiModelProperty("访问量")
    private Integer visitNums;

    @ApiModelProperty("人均访问量")
    private Double averageVisitNums;

    @ApiModelProperty("访客数")
    private Integer userNums;

    @ApiModelProperty("新访客数")
    private Integer newUserNums;

    @ApiModelProperty("新访客数")
    private Integer oldUserNum;

    @ApiModelProperty("系统类型 0:全部 1:pc  2:h5  3:小程序 4:安卓  5:ios")
    private Integer systemType;

    @ApiModelProperty("增长率")
    private FlowAnalysisRatioVO ratio;

    /**
     * 数据类型 1：新数据  2：旧数据
     */
    private Boolean newData;
    /**
     * 时间(周、月)
     */
    private Integer createDate;
    /**
     * 时间(日)
     */
    private Date day;


    public Boolean getNewData() {
        return newData;
    }

    public void setNewData(Boolean newData) {
        this.newData = newData;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Integer createDate) {
        this.createDate = createDate;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Long getUvAmount() {
        return uvAmount;
    }

    public void setUvAmount(Long uvAmount) {
        this.uvAmount = uvAmount;
    }

    public Long getCustomerAmount() {
        return customerAmount;
    }

    public void setCustomerAmount(Long customerAmount) {
        this.customerAmount = customerAmount;
    }

    public Integer getLossUser() {
        return lossUser;
    }

    public void setLossUser(Integer lossUser) {
        this.lossUser = lossUser;
    }

    public Double getLossUserRate() {
        return lossUserRate;
    }

    public void setLossUserRate(Double lossUserRate) {
        this.lossUserRate = lossUserRate;
    }

    public Integer getPlusShopCart() {
        return plusShopCart;
    }

    public void setPlusShopCart(Integer plusShopCart) {
        this.plusShopCart = plusShopCart;
    }

    public Integer getPlusShopCartUser() {
        return plusShopCartUser;
    }

    public void setPlusShopCartUser(Integer plusShopCartUser) {
        this.plusShopCartUser = plusShopCartUser;
    }

    public Long getPlaceOrderAmount() {
        return placeOrderAmount;
    }

    public void setPlaceOrderAmount(Long placeOrderAmount) {
        this.placeOrderAmount = placeOrderAmount;
    }

    public Integer getPlaceOrderUser() {
        return placeOrderUser;
    }

    public void setPlaceOrderUser(Integer placeOrderUser) {
        this.placeOrderUser = placeOrderUser;
    }

    public Double getPlaceOrderRate() {
        return placeOrderRate;
    }

    public void setPlaceOrderRate(Double placeOrderRate) {
        this.placeOrderRate = placeOrderRate;
    }

    public Long getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Long payAmount) {
        this.payAmount = payAmount;
    }

    public Integer getPayUser() {
        return payUser;
    }

    public void setPayUser(Integer payUser) {
        this.payUser = payUser;
    }

    public Double getPayRate() {
        return payRate;
    }

    public void setPayRate(Double payRate) {
        this.payRate = payRate;
    }

    public Integer getVisitNums() {
        return visitNums;
    }

    public void setVisitNums(Integer visitNums) {
        this.visitNums = visitNums;
    }

    public Double getAverageVisitNums() {
        return averageVisitNums;
    }

    public void setAverageVisitNums(Double averageVisitNums) {
        this.averageVisitNums = averageVisitNums;
    }

    public Integer getUserNums() {
        return userNums;
    }

    public void setUserNums(Integer userNums) {
        this.userNums = userNums;
    }

    public Integer getNewUserNums() {
        return newUserNums;
    }

    public void setNewUserNums(Integer newUserNums) {
        this.newUserNums = newUserNums;
    }

    public Integer getOldUserNum() {
        return oldUserNum;
    }

    public void setOldUserNum(Integer oldUserNum) {
        this.oldUserNum = oldUserNum;
    }

    public Integer getSystemType() {
        return systemType;
    }

    public void setSystemType(Integer systemType) {
        this.systemType = systemType;
    }


    public FlowAnalysisRatioVO getRatio() {
        return ratio;
    }

    public void setRatio(FlowAnalysisRatioVO ratio) {
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return "FlowAnalysisVO{" +
                "newData=" + newData +
                ", dateTime='" + dateTime + '\'' +
                ", createDate=" + createDate +
                ", day=" + day +
                ", UVAmount=" + uvAmount +
                ", customerAmount=" + customerAmount +
                ", lossUser=" + lossUser +
                ", lossUserRate=" + lossUserRate +
                ", plusShopCart=" + plusShopCart +
                ", plusShopCartUser=" + plusShopCartUser +
                ", placeOrderAmount=" + placeOrderAmount +
                ", placeOrderUser=" + placeOrderUser +
                ", placeOrderRate=" + placeOrderRate +
                ", payAmount=" + payAmount +
                ", payUser=" + payUser +
                ", payRate=" + payRate +
                ", visitNums=" + visitNums +
                ", averageVisitNums=" + averageVisitNums +
                ", userNums=" + userNums +
                ", newUserNums=" + newUserNums +
                ", oldUserNum=" + oldUserNum +
                ", systemType=" + systemType +
                ", ratio=" + ratio +
                '}';
    }
}
