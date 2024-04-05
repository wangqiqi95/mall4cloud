package com.mall4j.cloud.api.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * DTO
 *
 * @author gmq
 * @date 2022-09-20 10:58:25
 */
@Data
public class SkuPriceLogDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("spu货号(123)")
    private String spuCode;

    @ApiModelProperty("sku色码(123-BBK)")
    private String priceCode;

    @ApiModelProperty("sku条码(123-BBK65)")
    private String skuCode;

    @ApiModelProperty("门店Id")
    private Long storeId;

    @ApiModelProperty("门店编码")
    private String storeCode;

    @ApiModelProperty("日志类型：0-同步商品基础数据 1-同步吊牌价 2-同步保护价 3-同步pos价 4-同步库存 5-商品批量改价 6-批量设置渠道价 7-批量取消渠道价 8-同步吊牌价重算门店pos价")
    private Integer logType;

    @ApiModelProperty("变更价格")
    private Long price;

    private Long oldPrice;

    @ApiModelProperty("变更库存")
    private Integer stock;

    @ApiModelProperty("同步库存类型: 1共享库存(可售库存) 2门店库存")
    private Integer stockType;

    @ApiModelProperty("业务id")
    private String businessId;

    @ApiModelProperty("业务类型")
    private Integer businessType;

    @ApiModelProperty("操作人")
    private String updateBy;

    @ApiModelProperty("操作时间")
    private Date updateTime;

    @ApiModelProperty("操作备注")
    private String remarks;

}
