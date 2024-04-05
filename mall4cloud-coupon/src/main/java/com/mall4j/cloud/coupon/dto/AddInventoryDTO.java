package com.mall4j.cloud.coupon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddInventoryDTO {
    private Long id;

    @ApiModelProperty(value = "调整库存数")
    private Integer num;
}
