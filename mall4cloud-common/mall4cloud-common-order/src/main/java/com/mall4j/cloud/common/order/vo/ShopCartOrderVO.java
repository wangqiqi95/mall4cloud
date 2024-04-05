package com.mall4j.cloud.common.order.vo;

import com.mall4j.cloud.common.order.vo.CouponOrderVO;
import com.mall4j.cloud.common.order.vo.ShopCartItemDiscountVO;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 单个店铺的订单信息
 * @author FrozenWatermelon
 */
public class ShopCartOrderVO implements Serializable{

    @ApiModelProperty(value = "订单id", required = true)
    private Long orderId;

    @ApiModelProperty(value = "店铺id", required = true)
    private Long shopId;

    @ApiModelProperty(value = "店铺名称", required = true)
    private String shopName;

    @ApiModelProperty("店铺类型1自营店 2普通店")
    private Integer shopType;

    @ApiModelProperty(value = "实际总值", required = true)
    private Long actualTotal;

    @ApiModelProperty(value = "商品总值", required = true)
    private Long total;

    @ApiModelProperty(value = "商品总数", required = true)
    private Integer totalCount;

    @ApiModelProperty(value = "平台等级免运费金额", required = true)
    private Long levelFreeTransfee;

    @ApiModelProperty(value = "店家包邮金额", required = true)
    private Long freeTransfee;

    @ApiModelProperty(value = "运费", required = true)
    private Long transfee;

    @ApiModelProperty(value = "促销活动优惠金额", required = true)
    private Long discountReduce;

    @ApiModelProperty(value = "优惠券优惠金额", required = true)
    private Long couponReduce;

    @ApiModelProperty(value = "平台优惠券优惠金额", required = true)
    private Long platformCouponReduce;

    @ApiModelProperty(value = "积分优惠金额", required = true)
    private Long scoreReduce;

    @ApiModelProperty(value = "使用积分", required = true)
    private Long useScore;

    @ApiModelProperty(value = "平台优惠金额", required = true)
    private Long platformAmount;

    @ApiModelProperty(value = "等级优惠金额", required = true)
    private Long levelReduce;

    /**
     * 这个是一个店铺的总分摊，如果要算店铺的单独分摊 = 总分摊 - 平台分摊
     */
    @ApiModelProperty(value = "店铺优惠金额(促销活动 + 优惠券 + 积分优惠金额 + 其他)", required = true)
    private Long shopReduce;

    @ApiModelProperty(value = "订单备注信息", required = true)
    private String remarks;

    @ApiModelProperty(value = "订单类型")
    private Integer orderType;

    @ApiModelProperty(value = "订单号")
    private String orderNumber;

    @ApiModelProperty(value = "购物车商品", required = true)
    private List<ShopCartItemDiscountVO> shopCartItemDiscounts;

    @ApiModelProperty(value = "整个店铺可以使用的优惠券列表", required = true)
    private List<CouponOrderVO> coupons;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public Long getActualTotal() {
        return actualTotal;
    }

    public void setActualTotal(Long actualTotal) {
        this.actualTotal = actualTotal;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Long getFreeTransfee() {
        return freeTransfee;
    }

    public void setFreeTransfee(Long freeTransfee) {
        this.freeTransfee = freeTransfee;
    }

    public Long getTransfee() {
        return transfee;
    }

    public void setTransfee(Long transfee) {
        this.transfee = transfee;
    }

    public Long getDiscountReduce() {
        return discountReduce;
    }

    public void setDiscountReduce(Long discountReduce) {
        this.discountReduce = discountReduce;
    }

    public Long getCouponReduce() {
        return couponReduce;
    }

    public void setCouponReduce(Long couponReduce) {
        this.couponReduce = couponReduce;
    }

    public Long getScoreReduce() {
        return scoreReduce;
    }

    public void setScoreReduce(Long scoreReduce) {
        this.scoreReduce = scoreReduce;
    }

    public Long getUseScore() {
        return useScore;
    }

    public void setUseScore(Long useScore) {
        this.useScore = useScore;
    }

    public Long getPlatformAmount() {
        return platformAmount;
    }

    public void setPlatformAmount(Long platformAmount) {
        this.platformAmount = platformAmount;
    }

    public Long getLevelReduce() {
        return levelReduce;
    }

    public void setLevelReduce(Long levelReduce) {
        this.levelReduce = levelReduce;
    }

    public Long getShopReduce() {
        return shopReduce;
    }

    public void setShopReduce(Long shopReduce) {
        this.shopReduce = shopReduce;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<ShopCartItemDiscountVO> getShopCartItemDiscounts() {
        return shopCartItemDiscounts;
    }

    public void setShopCartItemDiscounts(List<ShopCartItemDiscountVO> shopCartItemDiscounts) {
        this.shopCartItemDiscounts = shopCartItemDiscounts;
    }

    public List<CouponOrderVO> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponOrderVO> coupons) {
        this.coupons = coupons;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getShopType() {
        return shopType;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setShopType(Integer shopType) {
        this.shopType = shopType;
    }
    @Override
    public String toString() {
        return "ShopCartOrderVO{" +
                "orderId=" + orderId +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", shopType='" + shopType + '\'' +
                ", actualTotal=" + actualTotal +
                ", total=" + total +
                ", totalCount=" + totalCount +
                ", freeTransfee=" + freeTransfee +
                ", transfee=" + transfee +
                ", discountReduce=" + discountReduce +
                ", couponReduce=" + couponReduce +
                ", platformCouponReduce=" + platformCouponReduce +
                ", scoreReduce=" + scoreReduce +
                ", useScore=" + useScore +
                ", platformAmount=" + platformAmount +
                ", levelReduce=" + levelReduce +
                ", shopReduce=" + shopReduce +
                ", remarks='" + remarks + '\'' +
                ", orderType=" + orderType +
                ", shopCartItemDiscounts=" + shopCartItemDiscounts +
                ", coupons=" + coupons +
                '}';
    }

    public Long getPlatformCouponReduce() {
        return platformCouponReduce;
    }

    public void setPlatformCouponReduce(Long platformCouponReduce) {
        this.platformCouponReduce = platformCouponReduce;
    }

    public Long getLevelFreeTransfee() {
        return levelFreeTransfee;
    }

    public void setLevelFreeTransfee(Long levelFreeTransfee) {
        this.levelFreeTransfee = levelFreeTransfee;
    }
}
