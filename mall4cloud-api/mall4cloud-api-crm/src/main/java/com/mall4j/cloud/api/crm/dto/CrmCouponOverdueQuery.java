package com.mall4j.cloud.api.crm.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class CrmCouponOverdueQuery {


    @ApiModelProperty(value = "到期开始时间")
    private String beginTime;

    @ApiModelProperty(value = "到期结束时间")
    private String endTime;

    @ApiModelProperty(value = "vipCodes")
    private String vipCode;

    // 当前时间
    private String date;

}
