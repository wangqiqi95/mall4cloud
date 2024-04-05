package com.mall4j.cloud.api.group.feign.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("下单送赠品信息入参app实体")
public class OrderGiftInfoAppDTO implements Serializable {
    @ApiModelProperty("门店id")
    private Long shopId;
    @ApiModelProperty("商品id")
    private Long commodityId;
}
