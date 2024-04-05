package com.mall4j.cloud.group.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("赠品库存实体")
public class OrderGiftStockVO implements Serializable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("下单赠品表id")
    private Integer orderGiftId;
    @ApiModelProperty("商品id")
    private Long commodityId;
    @ApiModelProperty("商品当前库存")
    private Integer commodityStock;
    @ApiModelProperty("赠送数量")
    private Integer giftNum;
    @ApiModelProperty(hidden = true)
    private Integer commodityMaxStock;
}
