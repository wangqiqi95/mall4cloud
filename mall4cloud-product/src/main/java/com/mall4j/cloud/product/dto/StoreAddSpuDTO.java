package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("添加门店商品")
public class StoreAddSpuDTO {

    @ApiModelProperty("门店商品")
    private List<Long> spuId;

}
