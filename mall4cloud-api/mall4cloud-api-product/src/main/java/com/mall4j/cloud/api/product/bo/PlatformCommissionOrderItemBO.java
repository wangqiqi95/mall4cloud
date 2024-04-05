package com.mall4j.cloud.api.product.bo;

/**
 * 订单项平台佣金需要的参数
 * @author FrozenWatermelon
 * @date 2021/5/24
 */
public class PlatformCommissionOrderItemBO {

    private Long shopId;

    private Long categoryId;

    private Double rate;

    private Long skuId;


    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    @Override
    public String toString() {
        return "PlatformCommissionOrderItemBO{" +
                "shopId=" + shopId +
                ", categoryId=" + categoryId +
                ", rate=" + rate +
                ", skuId=" + skuId +
                '}';
    }
}

