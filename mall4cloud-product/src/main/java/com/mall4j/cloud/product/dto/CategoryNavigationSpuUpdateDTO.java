package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 分类导航商品分类修改DTO
 * @date 2023/6/19
 */
@Data
public class CategoryNavigationSpuUpdateDTO {

    /**
     * 分类ID
     */
    @ApiModelProperty("分类ID")
    @NotNull(message = "categoryId 不能为空")
    private Long categoryId;

    /**
     * 商品ID
     */
    @ApiModelProperty("商品ID")
    @NotNull(message = "spuId 不能为空")
    private Long spuId;

    /**
     * 更新后的分类ID
     */
    @ApiModelProperty("更新后的分类ID")
    @NotNull(message = "updatedCategoryId 不能为空")
    private Long updatedCategoryId;
}
