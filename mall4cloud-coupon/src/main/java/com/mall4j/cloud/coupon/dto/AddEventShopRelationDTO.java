package com.mall4j.cloud.coupon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddEventShopRelationDTO {

    @ApiModelProperty(value = "店铺ID（plafrom库tz_store表主键）")
    private Long shopId;

    @ApiModelProperty(value = "店铺CODE")
    private String storeCode;

}
