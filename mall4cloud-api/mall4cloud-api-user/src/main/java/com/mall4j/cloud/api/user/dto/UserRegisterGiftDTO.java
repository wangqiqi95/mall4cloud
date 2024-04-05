package com.mall4j.cloud.api.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserRegisterGiftDTO {

    @ApiModelProperty("用户id")
    private Long userId;
    @ApiModelProperty("门店id")
    private Long storeId;

}
