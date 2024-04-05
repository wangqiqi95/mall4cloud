package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("商品池详情添加实体")
public class MealCommodityPoolDetailDTO implements Serializable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("商品池表Id")
    private Integer commodityPoolId;
    @ApiModelProperty("商品id")
    private Long commodityId;
    @ApiModelProperty("参与方式 0 按货号 1按条码 2按skuCode")
    private Integer participationMode;
    @ApiModelProperty("参与条码集合")
    private String barCodes;
}
