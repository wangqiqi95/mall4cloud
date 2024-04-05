package com.mall4j.cloud.coupon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;

import javax.validation.constraints.NotNull;

@Data
public class UpdateChooseMemberEventStockDTO {

    @NotNull(message = "活动ID为必传项")
    @ApiModelProperty("活动ID")
    private Integer eventId;

    @NotNull(message = "增加库存量为必传项")
    @ApiModelProperty("增加库存量")
    private Integer addStockNum;

}
