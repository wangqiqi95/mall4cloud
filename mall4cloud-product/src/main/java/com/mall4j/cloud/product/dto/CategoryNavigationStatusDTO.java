package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分类导航状态DTO
 * @date 2023/6/13
 */
@Data
public class CategoryNavigationStatusDTO {

    /**
     * 分类ID
     */
    @ApiModelProperty("分类ID")
    private Long categoryId;

    /**
     * 启用或禁用
     */
    @ApiModelProperty("启用或禁用 1启用 0禁用")
    private Integer isEnable;

}
