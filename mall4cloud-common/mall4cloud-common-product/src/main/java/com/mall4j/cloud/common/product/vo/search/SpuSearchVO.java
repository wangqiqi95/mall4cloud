package com.mall4j.cloud.common.product.vo.search;

import com.mall4j.cloud.common.product.vo.SpuActivityAppVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author FrozenWatermelon
 * @date 2020/11/17
 */
public class SpuSearchVO {

    @ApiModelProperty(value = "商品id")
    private Long spuId;

    @ApiModelProperty(value = "商品名称")
    private String spuName;

    @ApiModelProperty(value = "卖点")
    private String sellingPoint;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("品牌id")
    private Long brandId;

    @ApiModelProperty(value = "商品售价")
    private Long priceFee;

    @ApiModelProperty(value = "市场价，整数方式保存")
    private Long marketPriceFee;

    @ApiModelProperty(value = "是否有库存")
    private Boolean hasStock;

    @ApiModelProperty(value = "销量")
    private Integer saleNum;

    @ApiModelProperty(value = "评论数")
    private Integer commentNum;

    @ApiModelProperty(value = "好评数")
    private Integer goodReviewNum;

    @ApiModelProperty("商品介绍主图")
    private String mainImgUrl;

    @ApiModelProperty("积分价格")
    private Long scoreFee;

    @ApiModelProperty("商品活动(满减、优惠券)")
    private SpuActivityAppVO spuActivity;

    @ApiModelProperty("活动id(团购、秒杀)")
    private Long activityId;

    @ApiModelProperty("活动价格")
    private Long activityPrice;

    @ApiModelProperty("商品状态")
    private Integer spuStatus;

    @ApiModelProperty("序号")
    private Integer seq;

    @ApiModelProperty("分销信息")
    private DistributionInfoVO distributionInfo;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    public DistributionInfoVO getDistributionInfo() {
        return distributionInfo;
    }

    public void setDistributionInfo(DistributionInfoVO distributionInfo) {
        this.distributionInfo = distributionInfo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getMainImgUrl() {
        return mainImgUrl;
    }

    public void setMainImgUrl(String mainImgUrl) {
        this.mainImgUrl = mainImgUrl;
    }

    public Long getScoreFee() {
        return scoreFee;
    }

    public void setScoreFee(Long scoreFee) {
        this.scoreFee = scoreFee;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public String getSellingPoint() {
        return sellingPoint;
    }

    public void setSellingPoint(String sellingPoint) {
        this.sellingPoint = sellingPoint;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getPriceFee() {
        return priceFee;
    }

    public void setPriceFee(Long priceFee) {
        this.priceFee = priceFee;
    }

    public Long getMarketPriceFee() {
        return marketPriceFee;
    }

    public void setMarketPriceFee(Long marketPriceFee) {
        this.marketPriceFee = marketPriceFee;
    }

    public Boolean getHasStock() {
        return hasStock;
    }

    public void setHasStock(Boolean hasStock) {
        this.hasStock = hasStock;
    }

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public SpuActivityAppVO getSpuActivity() {
        return spuActivity;
    }

    public void setSpuActivity(SpuActivityAppVO spuActivity) {
        this.spuActivity = spuActivity;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getActivityPrice() {
        return activityPrice;
    }

    public void setActivityPrice(Long activityPrice) {
        this.activityPrice = activityPrice;
    }

    public Integer getSpuStatus() {
        return spuStatus;
    }

    public void setSpuStatus(Integer spuStatus) {
        this.spuStatus = spuStatus;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getGoodReviewNum() {
        return goodReviewNum;
    }

    public void setGoodReviewNum(Integer goodReviewNum) {
        this.goodReviewNum = goodReviewNum;
    }

    @Override
    public String toString() {
        return "SpuSearchVO{" +
                "spuId=" + spuId +
                ", spuName='" + spuName + '\'' +
                ", sellingPoint='" + sellingPoint + '\'' +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", brandId=" + brandId +
                ", priceFee=" + priceFee +
                ", marketPriceFee=" + marketPriceFee +
                ", hasStock=" + hasStock +
                ", saleNum=" + saleNum +
                ", commentNum=" + commentNum +
                ", goodReviewNum=" + goodReviewNum +
                ", mainImgUrl='" + mainImgUrl + '\'' +
                ", scoreFee=" + scoreFee +
                ", spuActivity=" + spuActivity +
                ", activityId=" + activityId +
                ", activityPrice=" + activityPrice +
                ", spuStatus=" + spuStatus +
                ", seq=" + seq +
                ", distributionInfo=" + distributionInfo +
                ", updateTime=" + updateTime +
                '}';
    }

}
