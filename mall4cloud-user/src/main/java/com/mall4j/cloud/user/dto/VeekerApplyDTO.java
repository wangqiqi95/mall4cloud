package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VeekerApplyDTO {

    @ApiModelProperty("门店ID")
    private Long storeId;
    @ApiModelProperty("员工ID")
    private Long staffId;
}
