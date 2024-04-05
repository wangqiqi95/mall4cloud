package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("下单赠品库存添加实体")
public class OrderGiftStockDTO implements Serializable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("下单赠品表id")
    private Integer orderGiftId;
    @ApiModelProperty("商品id")
    private Long commodityId;
    @ApiModelProperty("商品当前库存")
    private Integer commodityStock;
    @ApiModelProperty("商品最大库存")
    private Integer commodityMaxStock;
}
