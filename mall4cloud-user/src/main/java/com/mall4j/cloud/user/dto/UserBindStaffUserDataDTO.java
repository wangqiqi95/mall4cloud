package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserBindStaffUserDataDTO {

    @ApiModelProperty("会员id")
    private Long userId;
    @ApiModelProperty("会员名称")
    private String userName;
}
