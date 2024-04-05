package com.mall4j.cloud.common.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author axin
 * @Date 2023-06-14
 **/
@Data
public class SpuSearchAttrDTO {
    @ApiModelProperty("性别筛选(男士/女士/中性/男孩/女孩/KIDS中性等)")
    private List<String> sex;

    @ApiModelProperty("商品中台分类筛选（FTW/APP/ACC）等")
    private List<String> styleType;

    @ApiModelProperty("规格属性值分组")
    private List<SpuGroupAttr> spuGroupAttrs;

    @Data
    public static class SpuGroupAttr{
        @ApiModelProperty("是否模糊匹配")
        private Boolean vague= false;

        @ApiModelProperty("要查询的属性值")
        private List<String> attrValue;
    }
}
