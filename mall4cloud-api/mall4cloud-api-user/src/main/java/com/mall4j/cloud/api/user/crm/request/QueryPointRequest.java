package com.mall4j.cloud.api.user.crm.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryPointRequest {

    @ApiModelProperty("unionId")
    private String unionId;

    @ApiModelProperty("userId")
    private String userId;

}
