package com.mall4j.cloud.coupon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EnableOrDisableDTO {

    @NotNull(message = "活动ID为必传项")
    @ApiModelProperty("活动ID")
    private Long eventId;
}
