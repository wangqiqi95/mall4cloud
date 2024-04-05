package com.mall4j.cloud.api.docking.skq_erp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "中台库存数据VO", description = "从中台同步获取库存的VO")
public class ShopSpuStockVO {

    @ApiModelProperty(value = "库存数量")
    private Integer availableStockNum;


    @ApiModelProperty(value = "sku编码")
    private String skuCode;


    @ApiModelProperty(value = "店铺编码")
    private String storeCode;


    @ApiModelProperty(value = "spu编码")
    private String spuCode;

}
