package com.mall4j.cloud.biz.dto.channels;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品价格检查DTO
 * @date 2023/3/19
 */
@Data
@ApiModel("视频号价格检查DTO")
public class PriceCheckDTO {


    @ApiModelProperty(value = "商品本身的skuId")
    private Long skuId;

    @ApiModelProperty(value = "视频号售价")
    private Long price;
}
