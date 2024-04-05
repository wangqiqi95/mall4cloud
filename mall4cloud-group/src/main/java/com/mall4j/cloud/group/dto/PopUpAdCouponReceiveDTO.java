package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PopUpAdCouponReceiveDTO {

    @ApiModelProperty(value = "广告ID列表")
    private List<Long> adIdList;

    @ApiModelProperty(value = " 门店ID")
    private Long storeId;
}
