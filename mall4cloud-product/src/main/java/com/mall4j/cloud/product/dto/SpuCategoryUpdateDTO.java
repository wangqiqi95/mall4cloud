package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("商品导航修改")
public class SpuCategoryUpdateDTO {
    @ApiModelProperty("分类id")
    private Long categoryId;
    @ApiModelProperty("商品id集合")
    private List<Long> spuIdList;
}
