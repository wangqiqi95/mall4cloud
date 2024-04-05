package com.mall4j.cloud.common.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/12/9
 */
@Data
public class CouponOrderVO {

    @ApiModelProperty("优惠券ID")
    private Long couponId;

    @ApiModelProperty("店铺ID")
    private Long shopId;

    @ApiModelProperty("优惠券名称")
    private String couponName;

    @ApiModelProperty("优惠券券码")
    private String couponCode;

    @ApiModelProperty(value = "金额限制类型（0：不限/1：满额）")
    private Integer amountLimitType;

    @ApiModelProperty("使用条件")
    private Long cashCondition;

    @ApiModelProperty("减免金额")
    private Long reduceAmount;

    @ApiModelProperty("折扣额度")
    private Double couponDiscount;

    @ApiModelProperty("最大折扣金额")
    private Long maxDeductionAmount;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty(value = "指定的商品spuid")
    private List<Long> spuIds;

    // 字符串类型的商品id数组
    private String commodityIds;

    @ApiModelProperty(value = "指定的商品sku priceCode")
    private List<String> priceCodes;
    // 字符串类型的商品 pricecode数组
    private String strPriceCodes;

    @ApiModelProperty(value = "商品限制类型（0：不限/1：不超过/2：不少于 3,区间）")
    private Short commodityLimitType;

    @ApiModelProperty(value = "商品限制件数")
    private Integer commodityLimitNum;
    @ApiModelProperty(value = "商品限制件数")
    private Integer commodityLimitMaxNum;

    @ApiModelProperty(value = "适用商品类型 0全部商品参与 1指定商品参与SPU 2指定商品不可用SPU 3指定商品参与SKU 4指定商品不可用SKU")
    private Integer suitableProdType;

    @ApiModelProperty(value = "是否可用")
    private Boolean canUse;

    @ApiModelProperty(value = "是否选中")
    private Boolean isChoose;

    @ApiModelProperty(value = "优惠券种类（0：普通优惠券/1：包邮券/2：券码导入/3：企业券）")
    private Short kind;

    @ApiModelProperty(value = "价格类型（0：吊牌价/1：实付金额）")
    private Short priceType;

    @ApiModelProperty(value = "crm优惠券id")
    private String crmCouponId;

    @ApiModelProperty(value = "优惠类型 1:代金券 2:折扣券 3:兑换券")
    private Integer couponType;

    @ApiModelProperty(value = "用户优惠券id")
    private Long couponUserId;

    @ApiModelProperty("优惠券来源信息（1：小程序添加/2：CRM同步优惠券）")
    private Short sourceType;

    @ApiModelProperty(value = "适用商品类型分类 1不限制 2指定分类")
    private Integer categoryScopeType;

    @ApiModelProperty(value = "适用商品类型分类集合")
    private List<String> categorys;

    @ApiModelProperty(value = "是否有原折扣限制 0否1是")
    private Integer disNoles;

    @ApiModelProperty(value = "折扣限制值（例如7折维护 7 ）")
    private BigDecimal disNolesValue;

    @ApiModelProperty(value = "是否支持抵扣到0元")
    private Integer issharePaytype;

    /**
     * 使用该优惠券的订单id
     */
    private Long orderId;
    
    @ApiModelProperty("纸质券券码")
    private String paperCouponCode;

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Long getCashCondition() {
        return cashCondition;
    }

    public void setCashCondition(Long cashCondition) {
        this.cashCondition = cashCondition;
    }

    public Long getReduceAmount() {
        return reduceAmount;
    }

    public void setReduceAmount(Long reduceAmount) {
        this.reduceAmount = reduceAmount;
    }

    public Double getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(Double couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "CouponOrderVO{" +
                "couponId=" + couponId +
                ", shopId=" + shopId +
                ", couponName='" + couponName + '\'' +
                ", cashCondition=" + cashCondition +
                ", reduceAmount=" + reduceAmount +
                ", couponDiscount=" + couponDiscount +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public List<Long> getSpuIds() {
        return spuIds;
    }

    public void setSpuIds(List<Long> spuIds) {
        this.spuIds = spuIds;
    }

    public Integer getSuitableProdType() {
        return suitableProdType;
    }

    public void setSuitableProdType(Integer suitableProdType) {
        this.suitableProdType = suitableProdType;
    }

    public Boolean getCanUse() {
        return canUse;
    }

    public void setCanUse(Boolean canUse) {
        this.canUse = canUse;
    }

    public Boolean getChoose() {
        return isChoose;
    }

    public void setChoose(Boolean choose) {
        isChoose = choose;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public Long getCouponUserId() {
        return couponUserId;
    }

    public void setCouponUserId(Long couponUserId) {
        this.couponUserId = couponUserId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
