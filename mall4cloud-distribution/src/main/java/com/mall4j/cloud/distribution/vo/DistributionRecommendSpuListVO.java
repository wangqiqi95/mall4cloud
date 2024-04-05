package com.mall4j.cloud.distribution.vo;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 分销专用SPU列表视图
 *
 * @author EricJeppesen
 * @date 2022/10/20 18:38
 */
public class DistributionRecommendSpuListVO {
    @ApiModelProperty("商品ID")
    private Long spuId;

    @ApiModelProperty("商品名称")
    private String spuName;

    @ApiModelProperty("商品编码")
    private String spuCode;

    @ApiModelProperty("商品介绍主图")
    private String mainImgUrl;

    @ApiModelProperty(value = "商品售价")
    private Long priceFee;

    @ApiModelProperty(value = "市场价，整数方式保存")
    private Long marketPriceFee;

    @ApiModelProperty("导购佣金")
    private BigDecimal guideRate;

    @ApiModelProperty("微客佣金")
    private BigDecimal shareRate;

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public String getSpuCode() {
        return spuCode;
    }

    public void setSpuCode(String spuCode) {
        this.spuCode = spuCode;
    }

    public String getMainImgUrl() {
        return mainImgUrl;
    }

    public void setMainImgUrl(String mainImgUrl) {
        this.mainImgUrl = mainImgUrl;
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

    public BigDecimal getGuideRate() {
        return guideRate;
    }

    public void setGuideRate(BigDecimal guideRate) {
        this.guideRate = guideRate;
    }

    public BigDecimal getShareRate() {
        return shareRate;
    }

    public void setShareRate(BigDecimal shareRate) {
        this.shareRate = shareRate;
    }

    @Override
    public String toString() {
        return "DistributionRecommendSpuListVO{" +
                "spuId=" + spuId +
                ", spuName='" + spuName + '\'' +
                ", spuCode='" + spuCode + '\'' +
                ", mainImgUrl='" + mainImgUrl + '\'' +
                ", priceFee=" + priceFee +
                ", marketPriceFee=" + marketPriceFee +
                ", guideRate=" + guideRate +
                ", shareRate=" + shareRate +
                '}';
    }
}
