package com.mall4j.cloud.biz.dto.channels.league;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description 启用禁用联盟商品
 * @Author axin
 * @Date 2023-02-21 13:37
 **/
@Data
public class DisableProductReqDto {
    @ApiModelProperty(value = "联盟商品id")
    @NotNull(message = "计划id不能为空")
    private Long id;

    @ApiModelProperty(value = "状态0 上架 1下架")
    @NotNull(message = "状态不能为空")
    private Integer status;
}
