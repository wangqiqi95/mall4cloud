package com.mall4j.cloud.biz.dto.channels.league;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 商品详情
 * @Author axin
 * @Date 2023-02-21 14:10
 **/
@Data
public class ItemDetail {
    @ApiModelProperty(value = "商品id")
    private String productId;

    @ApiModelProperty(value = "特殊商品计划id")
    private String infoId;

    @ApiModelProperty(value = "spu名称")
    private String name;

    @ApiModelProperty(value = "spu编码")
    private String spuCode;

    @ApiModelProperty(value = "吊牌价")
    private Long marketPriceFee;

    @ApiModelProperty(value = "视频号售价")
    private Long livePriceFee;

    @ApiModelProperty(value = "商品分佣比例 0-90")
    private Integer ratio;

    @ApiModelProperty(value = "状态0启用 1禁用")
    private Integer status;

    @ApiModelProperty(value = "图片")
    private String headImgs;

}
