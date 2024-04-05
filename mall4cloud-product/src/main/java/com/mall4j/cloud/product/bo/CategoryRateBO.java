package com.mall4j.cloud.product.bo;

/**
 * 店铺分类佣金比例
 * @author FrozenWatermelon
 * @date 2021/5/24
 */
public class CategoryRateBO {
    private Long categoryId;

    private Double rate;

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

    @Override
    public String toString() {
        return "CategoryShopRateBO{" +
                ", categoryId=" + categoryId +
                ", rate=" + rate +
                '}';
    }
}
