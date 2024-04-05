package com.mall4j.cloud.api.product.bo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Author lth
 * @Date 2021/8/13 15:13
 */
public class SpuSimpleBO {

    @ApiModelProperty("spuId")
    private Long spuId;

    @ApiModelProperty("商品id列表")
    private List<Long> spuIds;

    @ApiModelProperty("商品名称")
    private String spuName;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("店铺id列表")
    private List<Long> shopIds;

    @ApiModelProperty("商品状态")
    private Integer status;

    @ApiModelProperty("商品主图")
    private String mainImgUrl;

    @ApiModelProperty("排序: 1.spuId 正序 2.spuId 倒序")
    private Integer seq;

    @ApiModelProperty("当前语言")
    private Integer lang;

    @ApiModelProperty("售价")
    private Long priceFee;

    @ApiModelProperty("市场价")
    private Long marketPriceFee;

    public String getMainImgUrl() {
        return mainImgUrl;
    }

    public void setMainImgUrl(String mainImgUrl) {
        this.mainImgUrl = mainImgUrl;
    }

    public List<Long> getSpuIds() {
        return spuIds;
    }

    public void setSpuIds(List<Long> spuIds) {
        this.spuIds = spuIds;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLang() {
        return lang;
    }

    public void setLang(Integer lang) {
        this.lang = lang;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

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

    public List<Long> getShopIds() {
        return shopIds;
    }

    public void setShopIds(List<Long> shopIds) {
        this.shopIds = shopIds;
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

    @Override
    public String toString() {
        return "SpuSimpleBO{" +
                "spuId=" + spuId +
                ", spuIds=" + spuIds +
                ", spuName='" + spuName + '\'' +
                ", shopId=" + shopId +
                ", shopIds=" + shopIds +
                ", status=" + status +
                ", mainImgUrl='" + mainImgUrl + '\'' +
                ", seq=" + seq +
                ", lang=" + lang +
                ", priceFee=" + priceFee +
                ", marketPriceFee=" + marketPriceFee +
                '}';
    }
}
