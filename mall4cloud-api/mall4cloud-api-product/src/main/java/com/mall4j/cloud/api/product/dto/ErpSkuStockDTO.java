package com.mall4j.cloud.api.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ErpSkuStockDTO {

    @ApiModelProperty(value = "表示线上或者线下,1：线上 0 线下")
    private String channelType;

    @ApiModelProperty(value = "可用库存量")
    private Integer availableStockNum;


    @ApiModelProperty(value = "商家sku编码")
    private String skuCode;
    private String productCode;


    @ApiModelProperty(value = "门店;店仓编码，当库存类型为2时必传")
    private String storeCode;
    private Long storeId;

    @ApiModelProperty(value = "库存类型(1-共享库存 2-门店库存)")
    private Integer stockType;

    @ApiModelProperty(value = "同步类型（1：增量同步，2：全量同步）")
    private Integer syncType;

    /**
     * 仅限内容日志存储使用
     */
    private String remark;
}
