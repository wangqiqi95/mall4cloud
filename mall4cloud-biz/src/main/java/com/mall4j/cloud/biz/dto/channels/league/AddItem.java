package com.mall4j.cloud.biz.dto.channels.league;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author axin
 * @Date 2023-02-20 17:11
 **/
@Data
public class AddItem {

    @ApiModelProperty(value = "视频号商品id")
    @NotBlank(message = "视频号商品id不能为空")
    private String outProductId;

    @ApiModelProperty(value = "商品spuid")
    @NotNull(message = "spuid不能为空")
    private Long spuId;

    @ApiModelProperty(value = "商品编码")
    @NotBlank(message = "商品编码不能为空")
    private String spuCode;

    @ApiModelProperty(value = "商品分佣比例 0-90")
    @Min(value = 0,message = "商品分佣比例0-90范围")
    @Max(value = 90,message = "商品分佣比例0-90范围")
    private Integer ratio;
}
