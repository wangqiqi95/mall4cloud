package com.mall4j.cloud.group.vo.app;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author YXF
 * @date 2021/3/26
 */
public class AppGroupSkuVO {

    @ApiModelProperty("SPU id")
    private Long spuId;

    @ApiModelProperty("拼团活动商品规格id")
    private Long groupSkuId;

    @ApiModelProperty(value = "商品规格Id")
    private Long skuId;

    @ApiModelProperty("售价，整数方式保存")
    private Long priceFee;

    @ApiModelProperty("市场价，整数方式保存")
    private Long marketPriceFee;

    @ApiModelProperty(value = "sku名称")
    private String skuName;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty(value = "sku图片")
    private String imgUrl;

    @ApiModelProperty(value = "销售属性组合字符串 格式是p1:v1;p2:v2")
    private String properties;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "状态")
    private Integer status;

    public Long getGroupSkuId() {
        return groupSkuId;
    }

    public void setGroupSkuId(Long groupSkuId) {
        this.groupSkuId = groupSkuId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }


    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AppGroupSkuVO{" +
                "spuId=" + spuId +
                ", groupSkuId=" + groupSkuId +
                ", skuId=" + skuId +
                ", priceFee=" + priceFee +
                ", marketPriceFee=" + marketPriceFee +
                ", skuName='" + skuName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", properties='" + properties + '\'' +
                ", stock=" + stock +
                '}';
    }
}
