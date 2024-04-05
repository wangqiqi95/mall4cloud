package com.mall4j.cloud.api.user.crm.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CrmQueryEditageEnquiryRequest {

    @ApiModelProperty("当前页 默认1")
    private int page;

    @ApiModelProperty("每页数量 默认20")
    private int pageSize;

}
