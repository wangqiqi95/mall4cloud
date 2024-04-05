package com.mall4j.cloud.user.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CheckVipCodeSingleTagCountDTO {

    @ApiModelProperty("用户卡号")
    private String vipCode;

    @ApiModelProperty("标签组列表")
    private List<Long> tagGroupIdList;


}
