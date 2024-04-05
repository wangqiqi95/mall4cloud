package com.mall4j.cloud.api.docking.skq_erp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="获取相关店铺的相关商品库存参数model", description="积分等级表")
public class GetStoresByShopsDto {

    @ApiModelProperty(value = "店铺编码List")
    private List<String> storeEcodes;

    @ApiModelProperty(value = "sku编码List")
    private List<String> skuEcodes;

    @ApiModelProperty(value = "页码")
    private Integer page;

    @ApiModelProperty(value = "条数")
    private Integer pageSize;

}
