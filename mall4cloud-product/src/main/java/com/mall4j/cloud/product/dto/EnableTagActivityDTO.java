package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EnableTagActivityDTO {
    @ApiModelProperty("营销标签活动id")
    @NotNull(message = "标签活动id不能为空")
    private Long id;
    @ApiModelProperty("营销标签活动状态 1 启用  0 禁用")
    @NotNull(message = "营销标签活动状态不能为空")
    private Integer enabled;
}
