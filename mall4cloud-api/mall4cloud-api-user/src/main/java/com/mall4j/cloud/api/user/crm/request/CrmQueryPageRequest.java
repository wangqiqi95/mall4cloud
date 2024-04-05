package com.mall4j.cloud.api.user.crm.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CrmQueryPageRequest extends CrmQueryEditageEnquiryRequest{

    @ApiModelProperty("微信unionId")
    private String unionId;

    @ApiModelProperty("微信userId")
    private String userId;

    @ApiModelProperty(value = "active",required = false)
    private String active;

    @ApiModelProperty(value = "job_code",required = false)
    private String job_code;

    @ApiModelProperty(value = "enquiry_code",required = false)
    private String enquiry_code;

}
