package com.mall4j.cloud.common.product.vo.search;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/11/17
 */
public class SearchItemVO {

    @ApiModelProperty(value = "品牌列表信息")
    private List<BrandSearchVO> brands;

    public List<BrandSearchVO> getBrands() {
        return brands;
    }

    public void setBrands(List<BrandSearchVO> brands) {
        this.brands = brands;
    }

    @Override
    public String toString() {
        return "SearchItemVO{" +
                "brands=" + brands +
                '}';
    }
}
