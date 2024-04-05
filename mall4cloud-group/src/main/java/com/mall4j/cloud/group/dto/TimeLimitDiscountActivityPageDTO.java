package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @luzhengxiang
 * @create 2022-03-10 4:58 PM
 **/
@Data
public class TimeLimitDiscountActivityPageDTO implements Serializable {
    @ApiModelProperty("活动名称")
    private String activityName;
    @ApiModelProperty("活动状态0未启用，2未开始，3已开始，4，已结束")
    private Integer status;
    @ApiModelProperty("适用门店id集合")
    private String shopIds;
    @ApiModelProperty("审核状态：0待审核 1审核通过 2驳回")
    private Integer checkStatus;
    @ApiModelProperty("类型1，限时调价。2，会员日活动调价")
    private Integer type = 1;
}