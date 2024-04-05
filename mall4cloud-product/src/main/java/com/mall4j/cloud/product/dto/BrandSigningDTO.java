package com.mall4j.cloud.product.dto;

import com.mall4j.cloud.api.product.dto.BrandShopDTO;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author lth
 * @Date 2021/5/13 17:29
 */
public class BrandSigningDTO {

    /**
     * 签约的平台品牌列表
     */
    @Valid
    private List<BrandShopDTO> platformBrandList;

    /**
     * 签约的自定义品牌列表
     */
    @Valid
    private List<BrandShopDTO> customizeBrandList;

    public List<BrandShopDTO> getPlatformBrandList() {
        return platformBrandList;
    }

    public void setPlatformBrandList(List<BrandShopDTO> platformBrandList) {
        this.platformBrandList = platformBrandList;
    }

    public List<BrandShopDTO> getCustomizeBrandList() {
        return customizeBrandList;
    }

    public void setCustomizeBrandList(List<BrandShopDTO> customizeBrandList) {
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
