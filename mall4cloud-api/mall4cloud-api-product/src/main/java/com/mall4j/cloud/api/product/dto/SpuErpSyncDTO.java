package com.mall4j.cloud.api.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SpuErpSyncDTO {

    @ApiModelProperty("spu名称")
    private String name;


    @ApiModelProperty("市场价")
    private Long marketPriceFee;

    @ApiModelProperty("售价")
    private Long priceFee;

    @ApiModelProperty("状态 1:enable, 0:disable, -1:deleted")
    private Integer status;

    @ApiModelProperty("商品规格列表")
    private List<SkuErpSyncDTO> skuList;

    @ApiModelProperty("总库存")
    private Integer totalStock;

    @ApiModelProperty("商品类型(0普通商品 1拼团 2秒杀 3积分)")
    private Integer spuType;

    private String styleCode;

    private String spuCode;

    @ApiModelProperty(value = "商品渠道（R线下渠道 T电商渠道 L清货渠道）")
    private String channelName;

    @ApiModelProperty(value = "折扣等级（错误数据或者-1,默认为0）")
    private String discount;

    @ApiModelProperty(value = "性别")
    private String sex;

}
