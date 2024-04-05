package com.mall4j.cloud.api.openapi.skq_erp.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FriendlyWalkGetWPopenIdResponse {

    private String openId;

    @ApiModelProperty(value = "状态 1:关注 2:取消关注")
    private Integer status;

}
