package com.mall4j.cloud.product.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * VO
 */
@Data
public class SkuPriceLogVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

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
    private String price;

    @ApiModelProperty("业务id")
    private String businessId;

    @ApiModelProperty("业务类型：1渠道价")
    private Integer businessType;

	@ApiModelProperty("更新时间")
	protected Date updateTime;

    @ApiModelProperty("操作人")
    private String updateBy;

}
