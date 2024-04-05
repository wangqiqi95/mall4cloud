package com.mall4j.cloud.order.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Pineapple
 * @date 2021/6/9 9:25
 */
public class OrderItemDetailVO {

    @ApiModelProperty("订单项id")
    private Long orderItemId;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("商品名称")
    private String spuName;

    @ApiModelProperty("规格名称")
    private String skuName;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty("购物车产品个数")
    private Integer count;

    @ApiModelProperty("商品总金额")
    private Long spuTotalAmount;

    /**
     * 商品实际金额 = 商品总金额 - 分摊的优惠金额
     */
    @ApiModelProperty("商品实际金额")
    private Long actualTotal;

    /**
     * 商家优惠金额[shareReduce-platformShareReduce]
     */
    @ApiModelProperty("商家优惠金额")
    private Long multishopReduce;

    @ApiModelProperty("平台优惠金额")
    private Long platformShareReduce;

    /**
     * 分销金额[推广员佣金+上级推广员佣金]
     */
    @ApiModelProperty("分销金额")
    private Long distributionAmount;

    @ApiModelProperty("使用积分")
    private Long useScore;

    @ApiModelProperty("分账比例")
    private Double rate;

    /**
     * 平台佣金(商品实际金额 * 分账比例)
     */
    @ApiModelProperty("平台佣金")
    private Long platformCommission;

    @ApiModelProperty("积分抵扣金额")
    private Long scoreAmount;

    @ApiModelProperty("会员折扣金额")
    private Long memberAmount;

    @ApiModelProperty("平台优惠券优惠金额")
    private Long platformCouponAmount;

    @ApiModelProperty("商家优惠券优惠金额")
    private Long shopCouponAmount;

    @ApiModelProperty("满减优惠金额")
    private Long discountAmount;

    @ApiModelProperty("商家运费减免金额")
    private Long freeFreightAmount;

    @ApiModelProperty("平台运费减免金额")
    private Long platformFreeFreightAmount;

    @ApiModelProperty("店铺改价优惠金额")
    private Long shopChangeFreeAmount;

    @ApiModelProperty("退款金额")
    private Long refundAmount;

    @ApiModelProperty("退货数量")
    private Integer refundCount;

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getSpuTotalAmount() {
        return spuTotalAmount;
    }

    public void setSpuTotalAmount(Long spuTotalAmount) {
        this.spuTotalAmount = spuTotalAmount;
    }

    public Long getActualTotal() {
        return actualTotal;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setActualTotal(Long actualTotal) {
        this.actualTotal = actualTotal;
    }

    public Long getMultishopReduce() {
        return multishopReduce;
    }

    public void setMultishopReduce(Long multishopReduce) {
        this.multishopReduce = multishopReduce;
    }

    public Long getPlatformShareReduce() {
        return platformShareReduce;
    }

    public void setPlatformShareReduce(Long platformShareReduce) {
        this.platformShareReduce = platformShareReduce;
    }

    public Long getDistributionAmount() {
        return distributionAmount;
    }

    public void setDistributionAmount(Long distributionAmount) {
        this.distributionAmount = distributionAmount;
    }

    public Long getUseScore() {
        return useScore;
    }

    public void setUseScore(Long useScore) {
        this.useScore = useScore;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Long getPlatformCommission() {
        return platformCommission;
    }

    public void setPlatformCommission(Long platformCommission) {
        this.platformCommission = platformCommission;
    }

    public Long getScoreAmount() {
        return scoreAmount;
    }

    public void setScoreAmount(Long scoreAmount) {
        this.scoreAmount = scoreAmount;
    }

    public Long getMemberAmount() {
        return memberAmount;
    }

    public void setMemberAmount(Long memberAmount) {
        this.memberAmount = memberAmount;
    }

    public Long getPlatformCouponAmount() {
        return platformCouponAmount;
    }

    public void setPlatformCouponAmount(Long platformCouponAmount) {
        this.platformCouponAmount = platformCouponAmount;
    }

    public Long getShopCouponAmount() {
        return shopCouponAmount;
    }

    public void setShopCouponAmount(Long shopCouponAmount) {
        this.shopCouponAmount = shopCouponAmount;
    }

    public Long getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Long discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Long getFreeFreightAmount() {
        return freeFreightAmount;
    }

    public void setFreeFreightAmount(Long freeFreightAmount) {
        this.freeFreightAmount = freeFreightAmount;
    }

    public Long getPlatformFreeFreightAmount() {
        return platformFreeFreightAmount;
    }

    public void setPlatformFreeFreightAmount(Long platformFreeFreightAmount) {
        this.platformFreeFreightAmount = platformFreeFreightAmount;
    }

    public Long getShopChangeFreeAmount() {
        return shopChangeFreeAmount;
    }

    public void setShopChangeFreeAmount(Long shopChangeFreeAmount) {
        this.shopChangeFreeAmount = shopChangeFreeAmount;
    }

    public Long getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Long refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Integer getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(Integer refundCount) {
        this.refundCount = refundCount;
    }

    @Override
    public String toString() {
        return "OrderItemDetailVO{" +
                "shopName='" + shopName + '\'' +
                ", spuName='" + spuName + '\'' +
                ", skuName='" + skuName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", categoryId=" + categoryId +
                ", count=" + count +
                ", spuTotalAmount=" + spuTotalAmount +
                ", actualTotal=" + actualTotal +
                ", multishopReduce=" + multishopReduce +
                ", platformShareReduce=" + platformShareReduce +
                ", distributionAmount=" + distributionAmount +
                ", useScore=" + useScore +
                ", rate=" + rate +
                ", platformCommission=" + platformCommission +
                ", scoreAmount=" + scoreAmount +
                ", memberAmount=" + memberAmount +
                ", platformCouponAmount=" + platformCouponAmount +
                ", shopCouponAmount=" + shopCouponAmount +
                ", discountAmount=" + discountAmount +
                ", freeFreightAmount=" + freeFreightAmount +
                ", platformFreeFreightAmount=" + platformFreeFreightAmount +
                ", shopChangeFreeAmount=" + shopChangeFreeAmount +
                ", refundAmount=" + refundAmount +
                ", refundCount=" + refundCount +
                '}';
    }
}
