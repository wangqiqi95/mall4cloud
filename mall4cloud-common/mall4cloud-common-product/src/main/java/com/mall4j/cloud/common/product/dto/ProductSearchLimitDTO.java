package com.mall4j.cloud.common.product.dto;


import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2021/06/29
 */
public class ProductSearchLimitDTO {

    public ProductSearchLimitDTO() {
    }

    public ProductSearchLimitDTO(Integer size) {
        this.size = size;
    }

    @ApiModelProperty(value = "店铺id列表")
    private List<Long> shopIds;

    @ApiModelProperty(value = "品牌id列表")
    private List<Long> brandIds;

    @ApiModelProperty(value = "商品分组id列表")
    private List<Long> tagIds;

    @ApiModelProperty(value = "大小")
    private Integer size;

    public List<Long> getShopIds() {
        return shopIds;
    }

    public void setShopIds(List<Long> shopIds) {
        this.shopIds = shopIds;
    }

    public List<Long> getBrandIds() {
        return brandIds;
    }

    public void setBrandIds(List<Long> brandIds) {
        this.brandIds = brandIds;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "ProductSearchLimitDTO{" +
                "shopIds=" + shopIds +
                ", brandIds=" + brandIds +
                ", tagIds=" + tagIds +
                ", size=" + size +
                '}';
    }
}
