package com.mall4j.cloud.common.product.vo;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @author Citrus
 * @date 2021/11/26 14:36
 */
public class SkuAddrVO {

    @ApiModelProperty("属性id")
    private Long skuId;

    @ApiModelProperty("SPU id")
    private Long spuId;

    @ApiModelProperty("shopId")
    private Long shopId;

    @ApiModelProperty("商品重量")
    private BigDecimal weight;

    @ApiModelProperty("商品体积")
    private BigDecimal volume;

    @ApiModelProperty("运费模板id")
    private Long deliveryTemplateId;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
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

    public Long getDeliveryTemplateId() {
        return deliveryTemplateId;
    }

    public void setDeliveryTemplateId(Long deliveryTemplateId) {
        this.deliveryTemplateId = deliveryTemplateId;
    }

    @Override
    public String toString() {
        return "SkuAddrVO{" +
                "skuId=" + skuId +
                ", spuId=" + spuId +
                ", shopId=" + shopId +
                ", weight=" + weight +
                ", volume=" + volume +
                ", deliveryTemplateId=" + deliveryTemplateId +
                '}';
    }
}
