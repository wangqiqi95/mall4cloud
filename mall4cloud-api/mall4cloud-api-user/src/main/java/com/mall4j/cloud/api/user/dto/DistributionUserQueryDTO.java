package com.mall4j.cloud.api.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author ZengFanChang
 * @Date 2022/01/23
 */
@Data
public class DistributionUserQueryDTO {

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("是否威客")
    private boolean witkey;

    @ApiModelProperty("门店ID")
    private Long storeId;

    @ApiModelProperty("是否招募会员")
    private boolean recruit;
}
