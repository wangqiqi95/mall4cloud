package com.mall4j.cloud.common.order.vo;

import com.mall4j.cloud.common.order.bo.DeliveryModeBO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020-11-20 15:47:32
 */
@Data
public class ShopCartItemVO implements Serializable {

    @ApiModelProperty(value = "加入购物车时间", required = true)
    private Date createTime;

    @ApiModelProperty(value = "购物车ID", required = true)
    private Long cartItemId;

    @ApiModelProperty("店铺ID")
    private Long shopId;

    @ApiModelProperty("产品ID")
    private Long spuId;

    @ApiModelProperty("产品货号")
    private String spuCode;

    @ApiModelProperty("SkuID")
    private Long skuId;

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty("中台分类")
    private String category;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("商品名称")
    private String spuName;

    @ApiModelProperty("sku名称")
    private String skuName;

    @ApiModelProperty("购物车产品个数")
    private Integer count;

    @ApiModelProperty("满减活动ID")
    private Long discountId;

    @ApiModelProperty("是否已经勾选")
    private Integer isChecked;

    @ApiModelProperty("售价，加入购物车时的商品价格")
    private Long priceFee;

    @ApiModelProperty("当前商品价格")
    private Long skuPriceFee;

    @ApiModelProperty("当前总价格(商品价格 * 数量)")
    private Long totalPriceFee;

    @ApiModelProperty("当前总价格(商品价格 * 数量)(带小数)")
    private Long totalPrice;

    @ApiModelProperty("配送方式json")
    private String deliveryMode;

    @ApiModelProperty("配送方式对象")
    private DeliveryModeBO deliveryModeBO;

    @ApiModelProperty("运费模板id")
    private Long deliveryTemplateId;

    @ApiModelProperty("商品重量")
    private BigDecimal weight;

    @ApiModelProperty("商品体积")
    private BigDecimal volume;

    @ApiModelProperty("商品图片")
    private String imgUrl;

    @ApiModelProperty("是否已收藏")
    private String isCollection;

    @ApiModelProperty(value = "积分价格",required=true)
    private Long scoreFee;

    @ApiModelProperty(value = "产品所需积分",required=true)
    private Long scorePrice;


    @ApiModelProperty(value = "总金额",required=true)
    private Long totalAmount;

    @ApiModelProperty(value = "商品实际金额 = 商品总金额 - 分摊的优惠金额 - 分摊的积分抵现金额")
    private Long actualTotal;

    @ApiModelProperty("sku规格信息列表")
    private List<OrderSkuLangVO> skuLangList;

    @ApiModelProperty("spu名称列表")
    private List<OrderSpuLangVO> spuLangList;

    @ApiModelProperty("分销员用户id")
    private Long distributionUserId;

    /**
     * 这里的分摊金额在重算之前，即以下代码执行之前，一直都是商家的分摊金额
     * confirmOrderManager.recalculateAmountWhenFinishingCalculatePlatform(shopCartOrderMerger);
     * 执行完毕之后，这里的分摊金额就变成了平台 + 商家的分摊金额
     */
    @ApiModelProperty(value = "分摊的优惠金额")
    private Long shareReduce;

    @ApiModelProperty(value = "平台分摊的优惠金额")
    private Long platformShareReduce;

    @ApiModelProperty(value = "能否分摊优惠券优惠金额(1可以 0不可以)")
    private Integer isShareReduce = 0;

    @ApiModelProperty(value = "是否为组合商品0普通商品，1组合商品")
    private Integer isCompose;

    @ApiModelProperty(value = "等级优惠金额", required = true)
    private Long levelReduce;

    @ApiModelProperty(value = "积分抵扣", required = true)
    private Long scoreReduce;


    @ApiModelProperty(value = "平台优惠券优惠金额", required = true)
    private Long platformCouponAmount;


    @ApiModelProperty(value = "商家优惠券优惠金额", required = true)
    private Long shopCouponAmount;

    @ApiModelProperty(value = "满减优惠金额", required = true)
    private Long discountAmount;

    @ApiModelProperty(value = "平台运费减免金额", required = true)
    private Long platformFreeFreightAmount;

    @ApiModelProperty(value = "商家运费减免金额", required = true)
    private Long freeFreightAmount;

    private Long storeId;

    private String styleCode;

    @ApiModelProperty("前端显示 ：skuCode")
    private String priceCode;
    private String channelName;
    @ApiModelProperty("门店库存(用于取价判断)")
    private Integer storeSkuStock;
    @ApiModelProperty("门店保护价(用于取价判断)")
    private Long storeProtectPrice;
    @ApiModelProperty("官店保护价(用于取价判断)")
    private Long skuProtectPrice;

    @ApiModelProperty("是否参加会员日调价活动 0否1是")
    private Integer friendlyFlag = 0;

    //是否使用虚拟门店价
    private boolean invateStorePriceFlag=false;

    //在会员日活动中是否可以使用优惠券标识 0否1是
    private Integer friendlyCouponUseFlag = 1;

    //在会员日活动中是否可以参加满减活动标识 0否1是
    private Integer friendlyDiscountFlag = 1;

    //虚拟门店活动设置的可用优惠券列表
    private List<Long> invateStoreCouponids;
    //虚拟门店活动id
    private Integer invateStoreActivityId;




    private List<DiscountOrderVO> discounts;

    private Long marketPriceFee;

    private Long marketTotalPrice;



    public String getSpuCode() {
        return spuCode;
    }

    public void setSpuCode(String spuCode) {
        this.spuCode = spuCode;
    }

    public Long getShareReduce() {
        return shareReduce;
    }

    public void setShareReduce(Long shareReduce) {
        this.shareReduce = shareReduce;
    }

    public Long getPlatformShareReduce() {
        return platformShareReduce;
    }

    public void setPlatformShareReduce(Long platformShareReduce) {
        this.platformShareReduce = platformShareReduce;
    }

    public String getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(String isCollection) {
        this.isCollection = isCollection;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public Long getPriceFee() {
        return priceFee;
    }

    public void setPriceFee(Long priceFee) {
        this.priceFee = priceFee;
    }

    public Long getSkuPriceFee() {
        return skuPriceFee;
    }

    public void setSkuPriceFee(Long skuPriceFee) {
        this.skuPriceFee = skuPriceFee;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Long getScorePrice() {
        return scorePrice;
    }

    public void setScorePrice(Long scorePrice) {
        this.scorePrice = scorePrice;
    }

    public List<OrderSkuLangVO> getSkuLangList() {
        return skuLangList;
    }

    public void setSkuLangList(List<OrderSkuLangVO> skuLangList) {
        this.skuLangList = skuLangList;
    }

    public List<OrderSpuLangVO> getSpuLangList() {
        return spuLangList;
    }

    public void setSpuLangList(List<OrderSpuLangVO> spuLangList) {
        this.spuLangList = spuLangList;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getTotalPriceFee() {
        return totalPriceFee;
    }

    public void setTotalPriceFee(Long totalPriceFee) {
        this.totalPriceFee = totalPriceFee;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getDistributionUserId() {
        return distributionUserId;
    }

    public void setDistributionUserId(Long distributionUserId) {
        this.distributionUserId = distributionUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getLevelReduce() {
        return levelReduce;
    }

    public void setLevelReduce(Long levelReduce) {
        this.levelReduce = levelReduce;
    }

    public Long getScoreReduce() {
        return scoreReduce;
    }

    public void setScoreReduce(Long scoreReduce) {
        this.scoreReduce = scoreReduce;
    }

    public DeliveryModeBO getDeliveryModeBO() {
        return deliveryModeBO;
    }

    public void setDeliveryModeBO(DeliveryModeBO deliveryModeBO) {
        this.deliveryModeBO = deliveryModeBO;
    }

    public Long getActualTotal() {
        return actualTotal;
    }

    public void setActualTotal(Long actualTotal) {
        this.actualTotal = actualTotal;
    }

    public List<DiscountOrderVO> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<DiscountOrderVO> discounts) {
        this.discounts = discounts;
    }

    public Integer getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Integer isChecked) {
        this.isChecked = isChecked;
    }

    public Long getDeliveryTemplateId() {
        return deliveryTemplateId;
    }

    public void setDeliveryTemplateId(Long deliveryTemplateId) {
        this.deliveryTemplateId = deliveryTemplateId;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public Integer getIsShareReduce() {
        return isShareReduce;
    }

    public void setIsShareReduce(Integer isShareReduce) {
        this.isShareReduce = isShareReduce;
    }

    public Integer getIsCompose() {
        return isCompose;
    }

    public void setIsCompose(Integer isCompose) {
        this.isCompose = isCompose;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    public Long getPlatformFreeFreightAmount() {
        return platformFreeFreightAmount;
    }

    public void setPlatformFreeFreightAmount(Long platformFreeFreightAmount) {
        this.platformFreeFreightAmount = platformFreeFreightAmount;
    }

    public Long getFreeFreightAmount() {
        return freeFreightAmount;
    }

    public void setFreeFreightAmount(Long freeFreightAmount) {
        this.freeFreightAmount = freeFreightAmount;
    }

    public Long getScoreFee() {
        return scoreFee;
    }

    public void setScoreFee(Long scoreFee) {
        this.scoreFee = scoreFee;
    }


    @Override
    public String toString() {
        return "ShopCartItemVO{" +
                "createTime=" + createTime +
                ", cartItemId=" + cartItemId +
                ", shopId=" + shopId +
                ", spuId=" + spuId +
                ", skuId=" + skuId +
                ", categoryId=" + categoryId +
                ", userId=" + userId +
                ", spuName='" + spuName + '\'' +
                ", skuName='" + skuName + '\'' +
                ", count=" + count +
                ", discountId=" + discountId +
                ", isChecked=" + isChecked +
                ", priceFee=" + priceFee +
                ", skuPriceFee=" + skuPriceFee +
                ", totalPriceFee=" + totalPriceFee +
                ", totalPrice=" + totalPrice +
                ", deliveryMode='" + deliveryMode + '\'' +
                ", deliveryModeBO=" + deliveryModeBO +
                ", deliveryTemplateId=" + deliveryTemplateId +
                ", weight=" + weight +
                ", volume=" + volume +
                ", imgUrl='" + imgUrl + '\'' +
                ", isCollection='" + isCollection + '\'' +
                ", scoreFee=" + scoreFee +
                ", scorePrice=" + scorePrice +
                ", totalAmount=" + totalAmount +
                ", actualTotal=" + actualTotal +
                ", skuLangList=" + skuLangList +
                ", spuLangList=" + spuLangList +
                ", distributionUserId=" + distributionUserId +
                ", shareReduce=" + shareReduce +
                ", platformShareReduce=" + platformShareReduce +
                ", isShareReduce=" + isShareReduce +
                ", isCompose=" + isCompose +
                ", levelReduce=" + levelReduce +
                ", scoreReduce=" + scoreReduce +
                ", platformCouponAmount=" + platformCouponAmount +
                ", shopCouponAmount=" + shopCouponAmount +
                ", discountAmount=" + discountAmount +
                ", platformFreeFreightAmount=" + platformFreeFreightAmount +
                ", freeFreightAmount=" + freeFreightAmount +
                ", discounts=" + discounts +
                '}';
    }
}
