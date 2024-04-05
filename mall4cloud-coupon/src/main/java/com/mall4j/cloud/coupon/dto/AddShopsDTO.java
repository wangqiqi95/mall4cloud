package com.mall4j.cloud.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
@ApiModel(description = "新增门店参数")
public class AddShopsDTO {
    @ApiModelProperty("活动id")
    private Long id;

    @ApiModelProperty("门店id列表")
    private List<Long> shops;
}
