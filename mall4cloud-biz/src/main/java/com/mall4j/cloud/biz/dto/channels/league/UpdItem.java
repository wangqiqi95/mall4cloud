package com.mall4j.cloud.biz.dto.channels.league;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @Description
 * @Author axin
 * @Date 2023-02-20 17:11
 **/
@Data
public class UpdItem {

    @ApiModelProperty(value = "商品id")
    private String productId;

    @ApiModelProperty(value = "特殊计划时必传")
    private String infoId;

    @ApiModelProperty(value = "商品分佣比例 0-90")
    @Min(value = 0,message = "商品分佣比例0-90范围")
    @Max(value = 90,message = "商品分佣比例0-90范围")
    private Integer ratio;
}
