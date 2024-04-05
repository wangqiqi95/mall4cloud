package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("预售活动商品添加实体")
public class AdvanceCommodityDTO implements Serializable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("预售活动表id")
    private Integer advanceId;
    @ApiModelProperty("条码")
    private String barCode;
    @ApiModelProperty("活动价")
    private BigDecimal activityPrice;
}
