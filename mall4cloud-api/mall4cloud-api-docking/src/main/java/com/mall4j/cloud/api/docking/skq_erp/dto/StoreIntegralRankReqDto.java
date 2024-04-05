package com.mall4j.cloud.api.docking.skq_erp.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description 门店积分抵现排行入参
 * @Author axin
 * @Date 2023-02-15 16:12
 **/
@Data
public class StoreIntegralRankReqDto {
    @ApiModelProperty(value = "年月(2023-02)", required = true)
    @NotBlank(message = "年月不能为空")
    private String month;

    @ApiModelProperty(value = "页码", required = true)
    private Integer pageNum;

    @ApiModelProperty(value = "页大小", required = true)
    private Integer pageSize=10;
}
