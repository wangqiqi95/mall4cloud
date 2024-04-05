package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SpuCommShowDTO {
    @ApiModelProperty("评价id")
    @NotNull(message = "评价id不能为空")
    private List<Long> ids;
    @ApiModelProperty("状态 0:不显示 1:显示 ")
    @NotNull(message = "状态id不能为空")
    private Integer status;
}
