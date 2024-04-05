package com.mall4j.cloud.api.user.crm.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryPointResponse {
    @ApiModelProperty("point")
    private String point;
}
