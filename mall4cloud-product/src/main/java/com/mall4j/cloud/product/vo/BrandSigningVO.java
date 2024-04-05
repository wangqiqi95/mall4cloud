package com.mall4j.cloud.product.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Author lth
 * @Date 2021/5/13 19:06
 */
public class BrandSigningVO {

    @ApiModelProperty(value = "签约的平台品牌列表")
    private List<BrandShopVO> platformBrandList;

    @ApiModelProperty(value = "签约的自定义品牌列表")
    private List<BrandShopVO> customizeBrandList;

    public List<BrandShopVO> getPlatformBrandList() {
        return platformBrandList;
    }

    public void setPlatformBrandList(List<BrandShopVO> platformBrandList) {
        this.platformBrandList = platformBrandList;
    }

    public List<BrandShopVO> getCustomizeBrandList() {
        return customizeBrandList;
    }

    public void setCustomizeBrandList(List<BrandShopVO> customizeBrandList) {
        this.customizeBrandList = customizeBrandList;
    }

    @Override
    public String toString() {
        return "BrandSigningDTO{" +
                "platformBrandList=" + platformBrandList +
                ", customizeBrandList=" + customizeBrandList +
                '}';
    }
}
