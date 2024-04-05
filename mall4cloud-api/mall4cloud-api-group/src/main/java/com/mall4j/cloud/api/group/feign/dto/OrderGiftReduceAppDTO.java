package com.mall4j.cloud.api.group.feign.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("下单送赠品扣减库存接口")
public class OrderGiftReduceAppDTO implements Serializable {
    @ApiModelProperty("活动id")
    private Integer activityId;
    @ApiModelProperty("商品id")
    private Long commodityId;
    @ApiModelProperty("扣减件数")
    private Integer num;
}
