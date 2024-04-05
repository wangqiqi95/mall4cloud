package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CommissionActivityStoreSearchDTO {

    @NotNull(message = "活动ID不能为空")
    @ApiModelProperty(value = "活动ID")
    private Long activityId;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "门店编码")
    private String storeCode;


}
